{-
This is old solver.
This is used for NOT productions teadmusbaas.
-}
module Lahendaja where

import System.IO

type StartState = State
type FinalState = State
type Movements = [State]
type NotAllowedStates = [State]
type Min = Int
type Max = Int

data Task = 
	Task StartState FinalState Movements NotAllowedStates Min Max deriving (Show)


type FileName = String
fullTreeProgram :: FileName -> IO ()
fullTreeProgram f = do
	contents <- readFile f
	let fileTask@(Task s _ _ _ _ _) = parseTask $ lines contents

	putStrLn $ show fileTask
	printNode $ fullTree (Node s [] []) fileTask 




-- ["[0,1]","[1,0]","[0,0] [1,1]", "[0,1,1,1,1,0,0,0] [1,0,0,0,0,1,1,1] [0,0,1,1,1,1,0,0] [1,1,0,0,0,0,1,1] [0,1,1,0,1,0,0,1] [1,0,0,1,0,1,1,0]"] -> Task
parseTask :: [String] -> Task
parseTask (mi:ma:start:end:moves:notallowed:[]) =
	Task (State $ read start) (State $ read end) (parseMoves moves) (parseMoves notallowed) (read mi) (read ma)


-- "[(-1),0] [0,1]" -> [State [(-1),0], State [0,1]]
parseMoves :: String -> [State]
parseMoves line = 
	map (\x -> State $ read x) $ words line












type Children = [Node]
type Parents = [Node] -- vajalik loopide kontrollimiseks

data Node  = 
	Node State Parents Children deriving(Show)

data State =
	State [Int] deriving(Show, Eq)

fullTree :: Node -> Task -> Node
fullTree i@ (Node s ps cs) t
	| length children == 0 
		= i
	| otherwise
		= Node s ps children
	where
		children = map (\x -> fullTree x t) (newChildren i t)

{-
-- syvitsi otsimine
buildNodeTree :: Node -> Node
buildNodeTree i@ (Node s ps cs) 
	| s == finalState -- FOUND SOLUTION!
		= i
	| otherwise 
		= Node s ps children
	where
		-- P6him6tteliselt "takeUntil solution"
		osad = span (not . solutionInThat) $ map buildNodeTree (newChildren i)
		children 
			| length (snd osad) == 0 -- ehk ei leidnud vastust
				= fst osad
			| otherwise -- leidus lahend ja see on teise peas
				= (fst osad) ++ ((head $ snd osad) : [])
-}

{-
-- vaatab ka Node lapsi, et kas seal on lahend
solutionInThat :: Node -> Task -> Bool
solutionInThat (Node s ps cs)
	| s == finalState
		= True
	| otherwise -- we need to look children!
		= or $ map solutionInThat cs
-}


-- Loob Node listi. Rakendades ylesande pystituse reegleid ja piiranguid. 
-- Nende uute Node-de lapsed ei looda siin!
type Parent = Node
newChildren :: Parent -> Task -> [Node]
newChildren p@ (Node s ps cs) t@ (Task _ _ mvs nas _ _)
	= filter (not . notAllowed ) $ filter (not . loop) $ map (\x -> Node x (ps ++ (p:[])) []) (childrenStates s)
	where 
		loop (Node x xs _)
			= elem x $ getStates xs
		notAllowed (Node x _ _)
			= elem x nas
		childrenStates s 
			= filter (not . invalidState t) (map (useRule s) mvs)

-- abimeetod
getStates :: [Node] -> [State]
getStates [(Node s _ _)] = [s]
getStates ((Node s _ _):ns) = s : (getStates ns) 

-- kui moni State on -1 voi 2 vms.. Ehk State peab alati olema 0 1 vahemikus
invalidState :: Task -> State -> Bool
invalidState (Task _ _ _ _ mi ma)(State xs)
	| length (filter (<mi) xs) >= 1 || length (filter (>ma) xs) >=1
		= True
	| otherwise
		= False


-- Reegli rakendamine State-le
useRule :: State -> State -> State
useRule (State xs) (State ys)
	= State (zipWith (+) xs ys) 


-- abimeetod
closestChildren :: Node -> [State]
closestChildren (Node _ _ cs) = map (\x -> getState x) cs

-- abimeetod
getState :: Node -> State
getState (Node s _ _ ) = s



-- Tegin m6ned muudatused, aga Core Data.Tree mooduli koodist. 
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