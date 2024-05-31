-- Daniel Duru CS 3360
-- Professor: Cheon


module Board where

-- variables for the players
mkPlayer = 1 :: Int
mkOpponent = 2 :: Int

-- creates a board of nxn
mkBoard :: Int -> [[Int]]
mkBoard n = take n (cycle [take n (cycle [0])])

-- Return the size of a board bd, n for an nxn board.
size :: [[Int]] -> Int
size bd = length bd

-- Return a row y of a board bd, where y is a 1-based index.
row :: Int -> [[Int]] -> [Int]
row y bd = bd !! (y - 1)

-- Return a column x of a board bd, where x is a 1-based index.
column :: Int -> [[Int]] -> [Int]
column x [] = []
column x bd = h !! col : column x t
	where (h:t) = bd
	      col = x - 1

{-Mark a place (x,y) in a board bd by a player p, where x and y 
     are 1-based column and row indices. Returns the full board edited. -}
mark :: Int -> Int -> [[Int]] -> Int -> [[Int]]
mark x 1 bd p = (markRow x h p) : t
	where (h:t) = bd
mark x y bd p = h : (mark x (y-1) t p)
	where (h:t) = bd
	

-- Marks a row at the 1-based index and returns it
markRow :: Int -> [Int] -> Int -> [Int]
markRow 1 (h:t) p = marker : t
	where marker = if h == 0 then p else h
markRow n (h:t) p = h : (markRow (n-1) t p)


-- Checks if place (x,y) of a board bd is unmarked (1-based)
isEmpty :: Int -> Int -> [[Int]] -> Bool
isEmpty x y bd = isMarkedBy x y bd 0

-- Checks if place (x,y) of a board bd is marked (1-based)
isMarked :: Int -> Int -> [[Int]] -> Bool
isMarked x y bd = not (isEmpty x y bd)

-- Checks if place (x,y) of a board is marked by a stone (1-based)
isMarkedBy :: Int -> Int -> [[Int]] -> Int -> Bool
isMarkedBy x y bd p = marker x y bd == p

-- Return the player of the stone placed on a place (x,y) (1-based)
marker :: Int -> Int -> [[Int]] -> Int
marker x y board = (!!) (row y board) col
	where col = x - 1

-- Checks if all places of a board bd are marked
isFull :: [[Int]] -> Bool
isFull bd = not (elem 0 (concat bd))



--CHECKING WINS

-- Check the veritcal wins, passed a list of col numbers from 1..size
checkWinVert :: [[Int]] -> Int -> Bool
checkWinVert bd p = elem True [hasWinSeq (column x bd) p | x <- [1..size bd]]

-- Check for any horizontal wins on the board
checkWinHorz :: [[Int]] -> Int -> Bool
checkWinHorz bd p = elem True [hasWinSeq h p | h <- bd]

-- Check for any wins to the diagonal down and left
checkWinRDiag :: [[Int]] -> Int -> Bool
checkWinRDiag bd p = elem True ([hasWinSeq h p | h <- diagonals])
    where (h:t) = diagonals
	  diagonals = [getDiagonal x y | x <- [1..bound ], y <- [1..bound ], isMainDiagonal x y || isAdjacentDiagonal x y]
          isMainDiagonal x y = x == 1 && y == 1
          isAdjacentDiagonal x y = (x == 1 || y == 1) && (bound - x >= 4 || bound - y >= 4)
          getDiagonal x y = [marker (x + i) (y + i) bd | i <- [0..bound  - 1], isInsideBoard (x + i) (y + i)]
          isInsideBoard x y = x >= 1 && y >= 1 && x <= bound  && y <= bound 
	  bound = size bd

-- Check for any wins to the diagonal down and left
checkWinLDiag :: [[Int]] -> Int -> Bool
checkWinLDiag bd p = elem True [hasWinSeq h p | h <- diagonals]
    where (h:t) = diagonals
	  diagonals = [getDiagonal x y | x <- reverse [1..bound ], y <- [1..bound ], isMainDiagonal x y || isAdjacentDiagonal x y]
          isMainDiagonal x y = x == bound && y == 1
          isAdjacentDiagonal x y = (x == bound || y == 1) && (x > 4 || bound - y >= 4)
          getDiagonal x y = [marker (x - i) (y + i) bd | i <- [0..bound  - 1], isInsideBoard (x - i) (y + i)]
          isInsideBoard x y = x >= 1 && y >= 1 && x <= bound  && y <= bound 
	  bound = size bd


-- Checks if there is a winning sequence in a particular row
hasWinSeq :: [Int] -> Int -> Bool
hasWinSeq [] p = False
hasWinSeq (h:t) p
	 | h == p && length t >= 4 = allP (take 4 t) || hasWinSeq t p
	 | otherwise = hasWinSeq t p
	where allP [] = True
      	      allP (h:t) = h == p && allP t

-- Checks if the player has won
isWonBy :: [[Int]] -> Int -> Bool
isWonBy bd p 
	| checkWinVert bd p = True
	| checkWinHorz bd p = True
	| checkWinRDiag bd p = True
	| checkWinLDiag bd p = True
	| otherwise = False


-- Checks if the game has reached a draw state
isDraw :: [[Int]] -> Bool
isDraw bd = not (isWonBy bd mkPlayer || isWonBy bd mkOpponent) && isFull bd

-- Checks if the game has reached an end state
isGameOver :: [[Int]] -> Bool
isGameOver bd = isDraw bd || isWonBy bd mkPlayer || isWonBy bd mkOpponent
