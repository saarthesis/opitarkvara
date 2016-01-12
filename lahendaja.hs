
{-
	1. Numbrid peavad oleam 0 voi 1. Ei tohi olla muud numbrid
-}


-- Mees, Rebane, Hani, Vili
-- vasak kallas on esimesed 4! ja parem kallas on viimased 4!

startState
	= State [1,1,1,1,0,0,0,0]
finalState 
	= State [0,0,0,0,1,1,1,1]

{-
	Tegevused, mida saab teha!

	mees
	mees ja rebane
	mees ja hani
	mees ja vili

-}

type Rule = State
rules :: [Rule]
rules 
	= [
	State [(-1),0,0,0,1,0,0,0], State [1,0,0,0,(-1),0,0,0],
	State [(-1),(-1),0,0,1,1,0,0],State [1,1,0,0,(-1),(-1),0,0],
	State [(-1),0,(-1),0,1,0,1,0],State [1,0,1,0,(-1),0,(-1),0],
	State [(-1),0,0,(-1),1,0,0,1],State [1,0,0,1,(-1),0,0,(-1)]
	]



{-
	States-s, mis ei tohi esineda!

	rebane ja hani ja vili
	hani ja vili
	rebane ja hani

-}
notAllowedStates :: [Rule]
notAllowedStates
	= [
	State [0,1,1,1,1,0,0,0], State [1,0,0,0,0,1,1,1],
	State [0,0,1,1,1,1,0,0], State [1,1,0,0,0,0,1,1],
	State [0,1,1,0,1,0,0,1], State [1,0,0,1,0,1,1,0]
	]


-- full
test1
	= printNode $ fullTree (Node startState [] [])

-- syvitsi
test2
	= printNode $ buildNodeTree (Node startState [] [])















type Children = [Node]
type Parents = [Node] -- vajalik loopide kontrollimiseks

data Node  = 
	Node State Parents Children deriving(Show)

data State =
	State [Int] deriving(Show, Eq)


-- buildNodeTree teeb suvitsi, aga see peaks tegema terve puu! 
fullTree :: Node -> Node
fullTree i@ (Node s ps cs) 
	| length children == 0 
		= i
	| otherwise
		= Node s ps children
	where
		children = map fullTree (newChildren i)


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


-- vaatab ka Node lapsi, et kas seal on lahend
solutionInThat :: Node -> Bool
solutionInThat (Node s ps cs)
	| s == finalState
		= True
	| otherwise -- we need to look children!
		= or $ map solutionInThat cs



-- Loob Node listi. Rakendades ylesande pystituse reegleid ja piiranguid. 
-- Nende uute Node-de lapsed ei looda siin!
type Parent = Node
newChildren :: Parent -> [Node]
newChildren p@ (Node s ps cs)
	= filter (not . notAllowed ) $ filter (not . loop) $ map (\x -> Node x (ps ++ (p:[])) []) (childrenStates s)
	where 
		loop (Node x xs _)
			= elem x $ getStates xs
		notAllowed (Node x _ _)
			= elem x notAllowedStates
		childrenStates s 
			= filter (not . invalidState) (map (useRule s) rules)

-- abimeetod
getStates :: [Node] -> [State]
getStates [(Node s _ _)] = [s]
getStates ((Node s _ _):ns) = s : (getStates ns) 

-- kui moni State on -1 voi 2 vms.. Ehk State peab alati olema 0 1 vahemikus
invalidState :: State -> Bool
invalidState (State xs)
	| length (filter (<0) xs) >= 1 || length (filter (>1) xs) >=1
		= True
	| otherwise
		= False


-- Reegli rakendamine State-le
useRule :: State -> Rule -> State
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