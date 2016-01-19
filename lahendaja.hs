import System.IO

-- 1) hetkel korvalharusi ei tseki nii nagu seal n2ites oli. kas peaks? ei usu. full tree ikkagi. ilma loopideta.
-- 2) hetkel voib olla probleeme loopidega otsimisega.. nt. armukadad [2,2,0] ykski parent ei olnud [2,2,0]
--  aga ikka lopetas...Look into it! Ah ikka koik korras. asi oli selles, et seal ei leidunud variante, mis juba kasutuses ei oleks olnud.. koik lapsed olid loops
-- 3) kas loop lapsed peaks genereerima? aga lihtsalt neile enam ei genereeri lapsi? 
-- voi loop lapsi ei peaks yldse genereerima?
-- 4) opikus loop lapsi ei looda. AGA luuakse notAllowedStates, aga lihtsalt sealt edasi ei minda
-- kyll aga "mees rebase ylesandes ei looda not allowed states"
-- 5) me saame teha nii,et lisame * state kirjeldustesse, kui vahet ei ole, mis seal teistes on. 
-- ja siis programm ise loob need koikvoimalikud stated 
-- << jah see on oluline! Teeb ylesande kirjelduse palju lyhemaks!
-- 6) samuti voiks lisada mitu loppolekut, mitte ainult 1. aga fulltrees see pole oluline nagunii
-- 7) kannude puhul on natukene raske. Peab m6tlema, et kuidas. see, et saad yhes koik 2ra kallata jne..
-- seal on palju eeltingimusi.. Produktsioonidega saaks h2sti
-- kui ainult 0 1 , siis tuleb p2ris palju tingimusi
-- ma ei n2e, kuidas kanne saaks ilam produktsioonideta teha. Sest seal on palju tingiusi.. 
-- yhest kannust teise kallamine nt.
-- ARVAN, et KANNUDEGA oleks VOIMALIK teha 0 ja 1 AGA siis on PALJU STATE. VAJA oleks *. Mis VOIMALADAKS KIRJA PANNA
-- 8) nt prgrammis 2 mode: 1) olekutega nii nagu paadid olid 2) produktsioonidega. nt kannude jaoks
-- 9) hetkel t88tab edasi isegi kui vastus leitud... kas tohiks?? -- seega kabe jaab praegu lahtiseks..sest ta ei saa aru, et nuud koik


-- kokkuvotvalt:
-- 1) Produktsioonid luua.
-- 2) * lyhendamaks state kirjapanemist
-- 3) kas luua edasi tipust, kust leiti vastus? Ei tohiks vast?
-- 4) kas genereerida mitte lubatuid tippe? "mees rebane .." ylesandes ei tethud. AGA
-- "armukaded " ylesandes loodi mitte lubatud tipud ja sealt edasi ei mindud lihtsalt.

-- 1. kannude ylesanne
-- 2. armukadedad
-- 2b. mees rebane hani
-- 3. malem2ng
-- 4. ahviylesanne kus kohas saaks teha produktsioonidle
-- 4b. misjoni2ride ylesanne
-- 5. kuidas TI ylesandeid Lispis lahendatakse
-- 6. 2x2 male
-- 7. 3m2ng
-- 8. 3x3 kabe
-- 9. saaks ka muuta 
-- 10. tripstrapstrull



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


-- buildNodeTree teeb suvitsi, aga see peaks tegema terve puu! 
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