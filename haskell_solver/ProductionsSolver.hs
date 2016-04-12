{-
ProductionsSolver reads Productions from file.
.. reads firstState and endStates.
.. generates tree starting form firstState, applying productionOnState method
.. displays full tree.
-}

{-
-- NOTES:
-- 1. Our program supports only integeres as values. No strings like "lipp", "must"
-- 2. Currently varible values can be only 0to9. If we want to get bigger. We need to create detailed parser. Takes a lot of time. Not necessary for kannud task.
-- 3. No parenthesis inside of teadmusbaas and times and dividings etc
-}

import ProductionsReader

import DataTypes
import Data.List
import System.Environment

import qualified Data.Text as T 
import qualified Data.Map as M
import Data.Char


test1 f = do
	--ps <- readProductions "../Executable_program_here/teadmusbaas/mees_rebane_hani_vili_PROD.txt"
	--let p = head ps
	--let s = State [0,0]
	--let ms = productionOnState p s
	--let vars = variables (start p) s
	--let ts = tupleIntoState $ calculateTuple $ replaceVariables vars (end p)
	--let cnd = replaceVariables vars (condition p)
	--let temp = map T.unpack $ T.splitOn (T.pack "&&") (T.pack cnd) 
	--let cnd_result_temp = map readStrCondition temp
	--let x1 = readStrCondition "(0<10)"
	--let cnd_result = and $ map readStrCondition temp
	--let cnd_result = and $ map readStrCondition $ map T.unpack $ T.splitOn (T.pack "&&") (T.pack cnd)
	--let ft = buildFullTreeFromFile2 "../Executable_program_here/teadmusbaas/mees_rebane_hani_vili_PROD.txt"	
	s <- readFirstState f 
	es <- readEndStates f
	ps <- readProductions f
	ns <- readNotAllowed f
	let t = Task s es ps ns
	--let n = Node s [] []
	let bug_state = State [10,0]
	let n = Node bug_state [] []
	let ft = fullTree n t
	let aps = applyProductions (productions t) (state n) 
	let ms = map (\x -> productionOnState x bug_state) ps
	let mms = productionOnState (ps !! 0) bug_state
	let bug_p = ps !! 0
	-- production on state testing
	let vars = variables (start bug_p) bug_state
	let rep_vars = replaceVariables vars (end bug_p)
	let calc_tuple = calculateTuple rep_vars
	let	ss = tupleIntoState $ calc_tuple
	let	x = replaceVariables vars (condition bug_p)
	let cnd = map T.unpack $ T.splitOn (T.pack "&&") (T.pack x)
	let parseCnd = if head cnd == "" then True else and $ map readStrCondition cnd
	let sm = statematch bug_state (start bug_p)
	let a = T.dropAround (\x -> x == ')' || x == '(') (T.pack rep_vars)
	let bs =T.splitOn (T.pack ",") a
	let cs = map (\x -> calculateString (T.unpack x)) $ bs
	let ns = concat $ intersperse "," $ map (\x->show x) cs
	--buildFullTreeFromFile f
	-- "(x,0)"
	return sm
--calculateTuple :: String -> String
--calculateTuple s =  "(" ++ ns ++ ")"
--	where 
--		a = T.dropAround (\x -> x == ')' || x == '(') (T.pack s)
--		bs =T.splitOn (T.pack ",") a
--		cs = map (\x -> calculateString (T.unpack x)) $ bs
--		ns = intersperse ',' $ map (intToDigit) cs

--productionOnState :: Production -> State -> Maybe State
--productionOnState p s
--	| condition p == "" && sm
--		= Just ss
--	| sm && parseCnd == True 
--		= Just ss
--	| otherwise
--		= Nothing
--	where
--		vars = variables (start p) s
--		ss = tupleIntoState $ calculateTuple $ replaceVariables vars (end p)
--		parseCnd = and $ map readStrCondition $ map T.unpack $ T.splitOn (T.pack "&&") (T.pack x)
--		x = replaceVariables vars (condition p)
--		sm = statematch s (start p)

--applyProductions :: [Production] -> State -> [State]
--applyProductions ps s = map out ds
--	where 
--		ms = map (\x -> productionOnState x s) ps 
--		fs = filter (/= Nothing) ms
--		ds = map head $ group $ sort fs
--		out (Just x) = x



{- this is for returning nodes -}
main = do 
	args <- getArgs
	if head(args) == "endstates" then do
		es <- readEndStates (args !! 1)
		putStr $ show es
	else if length args == 1 then do
		tree <- buildFullTreeFromFile2 (head args)
		putStr $ show tree
		--putStr $ show (Node (State [1,1,0,0]) [] [])
	else do
		return ()

{-
main = do 
	arg <- getArgs
	case arg of
		[f] -> buildFullTreeFromFile f
		_ -> return ()
-}


buildFullTreeFromFile f = do 
	s <- readFirstState f 
	es <- readEndStates f
	ps <- readProductions f
	ns <- readNotAllowed f
	printNode $ fullTree (Node s [] []) (Task s es ps ns) 

buildFullTreeFromFile2 f = do 
	s <- readFirstState f 
	es <- readEndStates f
	ps <- readProductions f
	ns <- readNotAllowed f
	return $ fullTree (Node s [] []) (Task s es ps ns) 


fullTree :: Node -> Task -> Node
fullTree n t 
	| or (map (== state n) (ends t)) -- this node is in the task "end nodes list"
		= n
	| length ns == 0 -- no children
		= n 
	| otherwise -- has children. repeat process
		= Node (state n) (parents n) $ map (\c -> fullTree c t) cs
	where
		aps = applyProductions (productions t) (state n) 
		ss = filter (\s -> not $ elem s $ notallowed t) $ aps 
		ps = parents n ++ [n] -- previous generations
		ns = map (\s -> Node s ps []) ss
		cs = filter (not . loop) ns
		loop x = elem (state x) (getStates ps) -- checks if any parent has same state

getStates :: [Node] -> [State]
getStates [(Node s _ _)] = [s]
getStates ((Node s _ _):ns) = s : (getStates ns) 

{-
Unique states!
-}
applyProductions :: [Production] -> State -> [State]
applyProductions ps s = map out ds
	where 
		ms = map (\x -> productionOnState x s) ps 
		fs = filter (/= Nothing) ms
		ds = map head $ group $ sort fs
		out (Just x) = x
{-
test1_applyProductions = do
	let f = "teadmusbaas/kannud.txt"
	ps <- readProductions f
	s <- readFirstState f 
	return $ applyProductions ps s
-}

skipComments :: [String] -> [String]
skipComments ls = filter (\l -> "--" /= take 2 l) ls

skipEmpty :: [String] -> [String]
skipEmpty ls = filter (\l -> l /= "") ls

skipNotAllowed :: [String] -> [String]
skipNotAllowed ls =  filter (\l -> "not allowed" /= take 11 l) ls

skipProductions :: [String] -> [String]
skipProductions ls =  filter (\l -> "(" /= take 1 l && "kui" /= take 3 l) ls

readFirstState :: FilePath -> IO State
readFirstState f = do
	cs <- readFile f
	return $ tupleIntoState $ head $ skipEmpty $ skipComments $ lines cs

readEndStates :: FilePath -> IO [State]
readEndStates f = do
	cs <- readFile f
	return $ map tupleIntoState $ words $ head $ tail $ skipEmpty $ skipComments $ lines cs

readProductions :: FilePath -> IO [Production]
readProductions f = do 
	cs <- readFile f
	return $ parseProductions $ skipNotAllowed $ skipEmpty $ skipComments $ lines cs

readNotAllowed :: FilePath -> IO [State]
readNotAllowed f = do 
	cs <- readFile f
	let notAllowedLines = skipProductions $ skipEmpty $ skipComments $ lines cs
	if length notAllowedLines == 0 then
		return []
	else 
		return $ map tupleIntoState $ words $ drop 11 $ head notAllowedLines


-- Tegin m6ned muudatused, aga muidu Core Data.Tree mooduli koodist. 
draw :: Node -> [String]
draw (Node (State x) _ ts0) = (show x) : drawSubTrees ts0
  where drawSubTrees [] = []
	drawSubTrees [t] =
		"|" : shift "`- " "   " (draw t)
	drawSubTrees (t:ts) =
		"|" : shift "+- " "|  " (draw t) ++ drawSubTrees ts

	shift first other = zipWith (++) (first : repeat other)

printNode :: Node -> IO ()
printNode = putStr . unlines . draw 