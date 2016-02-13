module ProductionsReader where

import DataTypes
import qualified Data.Text as T 
import qualified Data.Map as M
import Data.Char
import Data.List


-- productionOnState (Production "" "(s,v)" "(0,v)") (State [2,3])
-- returns: Just $ State [0,3]
-- AND
-- productionOnState (Production "(s>=3-v)" "(s,v)" "(s-3+v,3)") (State [3,2])
-- returns: Just $ State [2,3]
-- AND
-- productionOnState (Production "(s<3-v)" "(s,v)" "(0,v+s)") (State [3,2])
-- returns: Nothing
-- AND
-- productionOnState (Production "" "(1,r,1,v)" "(0,r,0,v)") (State [1,1,1,1])
-- returns: Just $ State [0,1,0,1]
-- AND
-- productionOnState (Production "" "(1,r,1,v)" "(0,r,0,v)") (State [0,1,1,1])
-- returns: Nothing
-- AND
-- productionOnState (Production "(h+v/=2)" "(1,1,h,v)" "(0,0,h,v)") (State [1,1,1,1])
-- returns: Nothing
-- AND
-- productionOnState (Production "(h+v/=2)&&(r+h/=2)" "(1,r,h,v)" "(0,r,h,v)") (State [1,1,1,1])
-- returns: Nothing
-- AND
-- productionOnState (Production "(h+v/=2)&&(r+h/=2)" "(1,r,h,v)" "(0,r,h,v)") (State [1,1,0,1])
-- returns: Just $ State [0,1,0,1]

productionOnState :: Production -> State -> Maybe State
productionOnState p s
	| condition p == "" && sm
		= Just ss
	| sm && parseCnd == True 
		= Just ss
	| otherwise
		= Nothing
	where
		vars = variables (start p) s
		ss = tupleIntoState $ calculateTuple $ replaceVariables vars (end p)
		parseCnd = and $ map readStrCondition $ map T.unpack $ T.splitOn (T.pack "&&") (T.pack x)
		x = replaceVariables vars (condition p)
		sm = statematch s (start p)

-- statematch (State [1,1,1,1]) "(1,r,1,v)" 
-- returns: True
-- statematch (State [0,1,1,1]) "(1,r,1,v)" 
-- returns: False
statematch :: State -> String -> Bool
statematch (State is) xs = and $ map (\(a,b) -> if a == b || isLetter b then True else False)  zs
	where
		zs = zip (rm $ show is) (rm xs)
		rm t = T.unpack $ T.dropAround (\x -> x == ')' || x == '(' || x == '[' || x == ']') (T.pack t)




-- [("s",2),("v",3)]
-- "(0,v)"
-- returns: "(0,3)"
replaceVariables :: [(String,Int)] -> String -> String
replaceVariables xs s = replaceVars m (M.keys m) s
	where
		m = M.fromList xs

{-
-- We assumes that, first two lines are start and end state.
(0,0)
(2,_)
(s,v)->(0,v)
(s,v)->(s,0)
(s,v)->(4,v)
(s,v)->(s,3)
kui (s >= 3-v) siis (s,v)->(s-3+v,3)
kui (s < 3-v) siis (s,v)->(0,v+s)
kui (v >= 4-s) siis (s,v)->(4,v-4+s)
kui (v < 4-s) siis (s,v)->(s+v,0)
returns: [Production "" "(s,v)" "(0,v)", ..., Production "v<4-s" "(s,v)" "(s+v,0)"]
-}
parseProductions :: [String] -> [Production]
parseProductions (_ : _ : ps) = parsePs ps


{-
(s,v)->(0,v)
(s,v)->(s,0)
(s,v)->(4,v)
(s,v)->(s,3)
kui (s >= 3-v) siis (s,v)->(s-3+v,3)
kui (s < 3-v) siis (s,v)->(0,v+s)
kui (v >= 4-s) siis (s,v)->(4,v-4+s)
kui (v < 4-s) siis (s,v)->(s+v,0)
returns: [Production "" "(s,v)" "(0,v)", ...]
-}
parsePs :: [String] -> [Production]
parsePs [] = []
parsePs (p:ps) = parseP p : parsePs ps


--"(s,v)->(0,v)"
--returns: Production "" "(s,v)" "(0,v)"
--AND
--"kui (s>=3-v) siis (s,v)->(s-3+v,3)"
--returns: Production "(s>=3-v)" "(s,v)" "(s-3+v,3)"
parseP :: String -> Production
parseP s 
	| length onSpace == 1 
		= Production "" (head onArrow) (last onArrow)
	| otherwise
		=  Production cnd first end
	where 
		onSpace = parts " " s
		onArrow = parts "->" s
		cnd = onSpace !! 1
		first = head $ parts "->" $ onSpace !! 3 
		end = last $ parts "->" $ onSpace !! 3  

parts :: String -> String -> [String]
parts on x = map T.unpack $ T.splitOn (T.pack on ) (T.pack x)




-- we can turn it into left and right side.. calculate them and then use middle comparison
-- we can use T.dropAround for removing parenthesis

-- "(3>=3-2)"
-- returns: True
-- AND
-- "(3<3-2)"
-- returns: False
-- readStrCondition "(3<3-2)"
-- returns False
-- readStrCondition "(3>=3-2)"
-- returns True
readStrCondition :: String -> Bool
readStrCondition s = f (intoCondition s)
	where
		f (a,b,c)
			| b == ">"
				= (calculateString a) > (calculateString c)
			| b == "<"
				= (calculateString a) < (calculateString c)
			| b == ">="
				= (calculateString a) >= (calculateString c)
			| b == "<="
				= (calculateString a) <= (calculateString c)
			| b == "=="
				= (calculateString a) == (calculateString c)
			| b == "/="
				= (calculateString a) /= (calculateString c)
-- "(3>=3-2)"
-- returns: ("3",">=","3-2")
intoCondition :: String -> (String,String,String)
intoCondition s = parser s 1 ("","","")

-- calculateTuple "(3-3+2,3)" 
-- returns: "(2,3)"
calculateTuple :: String -> String
calculateTuple s =  "(" ++ ns ++ ")"
	where 
		a = T.dropAround (\x -> x == ')' || x == '(') (T.pack s)
		bs =T.splitOn (T.pack ",") a
		cs = map (\x -> calculateString (T.unpack x)) $ bs
		ns = intersperse ',' $ map (intToDigit) cs

	

-- "3"
-- returns 3
-- AND
-- "3-2+4"
-- returns 5
calculateString :: String -> Int
calculateString xs = calcStr Nothing xs

-- Nothing 
-- "3-2+4"
-- returns: 5
calcStr :: Maybe Int -> String -> Int
calcStr (Just i) [] = i
calcStr Nothing (x:xs) = calcStr (Just $ digitToInt x) xs
calcStr (Just i) (a:b:xs)
	| a == '-'
		= calcStr (Just (i - (digitToInt b))) xs
	| a == '+'
		= calcStr (Just (i + (digitToInt b))) xs 




--parser "(3>=3-2)" 1 ("","","")
--("3",">=","3-2")
parser :: String -> Int -> (String, String, String) -> (String, String, String)
parser (x:xs) i (a,b,c)
	| i == 1 && x == '('
			= parser xs i (a,b,c)
	| i == 1 && (isDigit x || x == '-' || x == '+')
			= parser xs i (a ++ (x : []), b, c) 
	| i == 1 -- comparison symbol found
			= parser xs 2 (a,b ++ (x : []), c)
	| i == 2 && isDigit x
			= parser xs 3 (a, b, c ++ (x : [])) 
	| i == 2
			= parser xs i (a,b ++ (x : []), c)
	| i == 3 && (isDigit x || x == '-' || x == '+')
			= parser xs i (a, b, c ++ (x : [])) 
	| i == 3 && x == ')'
			= (a,b,c)



-- "(0,3)"
-- returns State [0,3]
tupleIntoState :: String -> State
tupleIntoState s = State $ numbers s



-- fromList [("s",2),("v",3)]
-- ["s","v"]
-- "(0,v)"
-- returns: "(0,3)"
replaceVars :: M.Map String Int -> [String] -> String -> String 
replaceVars _ [] w = w
replaceVars m (k:ks) w 
	= case M.lookup k m of 
		Just v -> replaceVars m ks (r v)
		Nothing -> replaceVars m ks w -- It should never get here. we should always find. but ok.
	where 
		r v = T.unpack $ T.replace (T.pack k) (T.pack $ show v) (T.pack w)

-- "(s,v)"
-- State [2,3]
-- returns: [("s",2),("v",3)]
variables :: String -> State -> [(String,Int)]
variables vs (State is) = zip (letters vs) is

-- "(a,b,c,d)"
-- returns: ["a","b","c","d"]
letters :: String -> [String]
letters s = map T.unpack $ T.splitOn (T.pack ",") (T.pack $ tail $ init s)

-- "(0,1,1,0)"
-- returns: [0,1,1,0]
numbers :: String -> [Int]
numbers s = map (\x -> read x :: Int) $ letters s

stringIntoChar :: String -> Char
stringIntoChar (x:[]) = x


