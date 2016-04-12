module DataTypes where 

data Task 
	= Task {
	firstS :: State,
	ends :: [State],
	productions :: [Production],
	notallowed :: [State]
} deriving(Show)

data Production 
	= Production {
		condition :: String,
		start :: String,
		end :: String
	} deriving(Show)


instance Show Node where
	show (Node s _ c) = "Node {state = "++(show s) ++", children = "++(show c)++"}" 

data Node  = 
	Node {
	state :: State,
	parents :: [Node],
	children :: [Node]
	}

data State =
	State [Int] deriving(Show, Eq, Ord)