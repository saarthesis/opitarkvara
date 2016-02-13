module DataTypes where 

data Task 
	= Task {
	firstS :: State,
	ends :: [State],
	productions :: [Production],
	notallowed :: [State]
}

data Production 
	= Production {
		condition :: String,
		start :: String,
		end :: String
	} deriving(Show)


data Node  = 
	Node {
	state :: State,
	parents :: [Node],
	children :: [Node]
	}deriving(Show)

data State =
	State [Int] deriving(Show, Eq, Ord)