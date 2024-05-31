-- Daniel Duru CS 3360
-- Professor: Cheon


module Main where

import Board
import System.Random

gameBoard = 15 :: Int
-- entry point of the game

main :: IO ()
main = do
	mode <- getGameMode
   	if mode == 1 then do
		putStrLn "\nPlayer vs Player Initiated\n"
		doPlay bd mkPlayer mkOpponent
  	else if mode == 2 then do
		putStrLn "\nPlayer vs Computer Initiated\n"
		cpuMode <- getCpuMode
		doPvC bd mkPlayer mkOpponent cpuMode
   	else return()
   	where bd = mkBoard gameBoard
	 

-- Prompts the user for a game mode
getGameMode :: IO (Int)
getGameMode = do
	putStrLn "1 = Player vs Player"
	putStrLn "2 = Player vs Computer"	
	putStrLn "-1 = Exit"
	putStr "Enter game mode: "
	input <- getLine
	let parsed = reads input :: [(Integer, String)] in
         	if length parsed == 0
         	then retry
         	else let (x, tail) = head parsed in
			if (x == 1 || x == 2 || x == -1) then return (fromIntegral x)
			else retry
	where retry = do
		putStrLn "Invalid input.\n"
		getGameMode

-- Prompts the user for a game mode
getCpuMode :: IO (Int)
getCpuMode = do
	putStrLn "1 = Smart Computer"
	putStrLn "2 = Random Computer"	
	putStr "Enter game mode: "
	input <- getLine
	let parsed = reads input :: [(Integer, String)] in
         	if length parsed == 0
         	then retry
         	else let (x, tail) = head parsed in
			if (x == 1 || x == 2) then return (fromIntegral x)
			else retry
	where retry = do
		putStrLn "Invalid input.\n"
		getCpuMode 

-- Plays the game out to completion (Player vs Player)
doPlay :: [[Int]] -> Int -> Int -> IO ()
doPlay bd p1 p2 = do
	(isOver, bd') <- doMove bd p1
	if isOver then return () else doPlay bd' p2 p1


-- Get the current players move on the current board
doMove :: [[Int]] -> Int -> IO (Bool, [[Int]])
doMove bd p = do
	printBoard bd
	(x,y) <- getXY bd p
	let bd' = mark x y bd p in
	   if x == -1 then do
		return (True, bd')
	   else if isGameOver bd'
	   	then if isWonBy bd' p
	     	     then do
			putStr ([playerToChar p] ++ " has won!")
			return (True, bd')
	     	else if isDraw bd'
	     	     then do 
			putStr "Draw."
	     		return (True, bd')
		else return(True, bd')
	   else return (False, bd')

-- computer things

-- Plays the game out to completion (Player vs Computer)
doPvC :: [[Int]] -> Int -> Int -> Int -> IO ()
doPvC bd p1 p2 mode = do
	(isOver, bd') <- doMove bd p1
	if not isOver 
	then do
		(isDone, cBoard) <- cpuMove bd' p2 mode
		if not isDone then doPvC cBoard p1 p2 mode
		else return()
	else return()


-- Get the current players move on the current board
cpuMove :: [[Int]] -> Int -> Int -> IO (Bool, [[Int]])
cpuMove bd p mode = do
	(x,y) <- getCpuMove bd p mode
	let bd' = mark x y bd p in
		if isWonBy bd' p
	     	     then do
			putStr ([playerToChar p] ++ " has won!")
			return (True, bd')
		else return (False, bd')

-- Get the current players move on the current board
cpuSmartMove :: [[Int]] -> Int -> IO (Bool, [[Int]])
cpuSmartMove bd p = do
	(x, y) <- getSmartXY bd p
	let bd' = mark x y bd p in
		if isWonBy bd' p
	     	     then do
			putStr ([playerToChar p] ++ " has won!")
			return (True, bd')
		else return (False, bd')


-- Chooses a random coordinate on the board
getRandomXY :: [[Int]] -> IO (Int, Int)
getRandomXY bd = do
	selection <- randomRIO (0,bound-1) :: IO Int
	return (possibles !! selection)
		where bound = length possibles
		      possibles = getPossibleMoves bd

-- Chooses a smart coordinate for the Computer
getSmartXY :: [[Int]] -> Int -> IO (Int, Int)
getSmartXY bd p = do
	selection <- randomRIO (0,bound-1) :: IO Int
	return (possibles !! selection)
		where bound = length possibles
		      possibles
		      	| (length smartMoves) == 0 = getPossibleMoves bd
		     	| otherwise = smartMoves 
			where smartMoves = getSmartMoves bd p

-- Gets the Computer's next move based on the mode and board
getCpuMove :: [[Int]] -> Int -> Int -> IO (Int, Int)
getCpuMove bd p mode = do
	(x, y) <- getRandomXY bd
	(z, w) <- getSmartXY bd p
	if mode == 2 then
		return (x,y)
	else return (z, w)
		
	
-- Generates a list of possible moves for the Computer
getPossibleMoves :: [[Int]] -> [(Int, Int)]
getPossibleMoves bd = [(x, y) | x <- [1..bound], y <- [1..bound], isEmpty x y bd]
	where bound = size bd

-- Gets a list of smart moves for the Computer to choose from
getSmartMoves :: [[Int]] -> Int -> [(Int, Int)]
getSmartMoves bd p = [((x + i), (y + j)) | (x,y) <- (hasMoves bd p), i <- [-1, 0, 1], j <- [-1, 0, 1], isInsideBoard (x + i) (y + j), isEmpty (x + i) (y + j) bd]
	where hasMoves bd p = [(x,y) | x <- [1..bound], y <- [1..bound], isMarkedBy x y bd p]
              isInsideBoard x y = x >= 1 && y >= 1 && x <= bound  && y <= bound 
	      bound = size bd	

--- player things

-- Gets a valid (x,y) value for the board
getXY :: [[Int]] -> Int -> IO (Int, Int)	
getXY bd p = do
	putStr ([playerToChar p] ++ "'s turn. ")
	putStr "Enter an X and Y (1-15) or -1 to quit: "
	line <- getLine
	let parsed = reads line :: [(Integer, String)] in
         	if length parsed == 0
         	then getXY'
         	else let (x, tail) = head parsed in
			if x == -1 then return (fromIntegral x, fromIntegral x)
           		else if x > 0 
           		then let second = reads tail :: [(Integer, String)] in
	     			if length second == 0
	     			then getXY'
	     			else let (y, _) = head second in
              				if y > 0
              				then if isEmpty (fromIntegral x) (fromIntegral y) bd
					     then return (fromIntegral x,fromIntegral y)
					     else getXY2
              				else getXY'
	   	else getXY'
	where getXY' = do
           	putStrLn "Invalid input!"
           	getXY bd p
	      getXY2 = do
	   	putStrLn "Already placed!"
	   	getXY bd p



-- Returns the character representation of the player "p"
playerToChar :: Int -> Char
playerToChar p 
	 | p == mkOpponent = 'X'
	 | p == mkPlayer = 'O'
	 | otherwise = '.'

-- Converts the board to it's string representation
boardToStr :: (Int -> Char) -> [[Int]] -> String
boardToStr playerToChar bd = rowsToStr [1..(size bd)] bd playerToChar 

rowsToStr :: [Int] -> [[Int]] -> (Int -> Char) -> String
rowsToStr [] bd f = ""
rowsToStr rows bd f = (show (mod h 10)) ++ "|" ++ rowToStr x f ++ rowsToStr t xs f
	where rowToStr (num:last) fn =  " " ++ [(fn num)] ++ rowToStr last fn
	      rowToStr [] fn = "\n"
	      (x:xs) = bd
              (h:t) = rows

	      

printBoard :: [[Int]] -> IO ()
printBoard bd = do
	putStr " x 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5\n"
	putStr "y ------------------------------\n"
	putStr (boardToStr playerToChar bd)