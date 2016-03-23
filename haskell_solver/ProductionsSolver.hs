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

import ProductionsReader(parseProductions, productionOnState, tupleIntoState)

import DataTypes
import Data.List
import System.Environment


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
		ss = filter (\s -> not $ elem s $ notallowed t) $ applyProductions (productions t) (state n) -- [State]	
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