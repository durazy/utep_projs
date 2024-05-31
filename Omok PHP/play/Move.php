<?php
// Daniel Duru Section 2

require_once '../common/constants.php';
require_once 'Board.php';

class Move {
    
    public $board;
    private $user;
    
    var $x;
    var $y;
    var $isWin;
    var $isDraw;
    var $row;
    
    //constructs the Move, checks for a draw, then checks for a win
    function __construct($x, $y, $board, $user, $test = false) {
        $this->board = $board;
        $this->user = $user;
        $this->x = $x;
        $this->y = $y;
        $this->isWin = false;
        $this->isDraw = false;
        $this->row = [];
        if (!$test) {
            $board->placePiece($x, $y, $user);
        }
        
        if ($user == PLAYER) {
            $this->checkDraw();
        }
        
        $longest_data = $this->checkLongest($user);
        if ($longest_data["length"] > 4) {
            $this->isWin = true;
            $this->row = $this->getWinRow($longest_data);
        }
        
    }
    
    //return decoded json array
    function toJson() {
       return array("x" => $this->x, "y" => $this->y, "isWin" => $this->isWin, 
           "isDraw" => $this->isDraw, "row" => $this->row);
    }
    
    
    /*
     * CHECKS
     * for all checks, return value will be like this: 
     *      array("longest" => [x,y] (start coordinate), "length" => value)
     * diagonals and veritcal start from the top, horizontal starts from the left
     * 
     */
    
    /* find the current longest streak of the given player
     * compares the 4 types of streaks
     * returns:
     * array(type of streak, coordinate of streak, length of streak)
     * always checks horizontal first
     * 1 = Horizontal
     * 2 = Vertical
     * 3 = Diagonal Right
     * 4 = Diagonal Left
     */
    function checkLongest($user) {
        $longest_type = HORIZONTAL;
        $longest_length = 0;
        $longest_start = [0,0]; //x,y
        
        //data for each longest
        $horz = $this->checkHorz($user);
        $vert = $this->checkVert($user);
        $diag_r = $this->checkDRight($user);
        $diag_l = $this->checkDLeft($user);
        
        //lengths to iterate
        $length = array (1 => $horz["length"], $vert["length"], $diag_r["length"], $diag_l["length"]);
        $coords = array (1 => $horz["coords"], $vert["coords"], $diag_r["coords"], $diag_l["coords"]);
        
        for ($i = 1; $i <= 4; $i++) {
            if ($length[$i] > $longest_length) {
                $longest_length = $length[$i];
                $longest_type = $i;
                $longest_start = $coords[$i];
            }
        }
        
        return array("type" => $longest_type, "coords" => $longest_start, "length" => $longest_length);
    }
    
    //gets the largest horizontal streak to the right of given coordinate
    function checkHorz($user) {
        $grid = $this->board->grid;
        $max = 0;
        $max_coord = [0,0];
        for ($i = 0; $i < BOARD_SIZE; $i++) {
            for ($j = 0; $j < BOARD_SIZE; $j++) {
                $streak = $this->horzStreak($i, $j, $user, $grid);
                if ($streak > $max) {
                    $max = $streak;
                    $max_coord = [$i,$j]; //x,y
                }
            }
        }
        
        return array ("coords" => $max_coord, "length" => $max);
    }
    // recursively checks the longest streak horizontally
    function horzStreak($x, $y, $user, $grid) {
        $sum = 0;
        if ($x < BOARD_SIZE && $x >= 0 && $y < BOARD_SIZE && $y >= 0) {
            if ($grid[$y][$x] == $user) { //remember its [y][x]
                return 1 + $this->horzStreak(++$x, $y, $user, $grid);
            }
        }
        return $sum;
    }
    
    
    //gets the largest vertical streak south of given coordinate
    function checkVert($user) {
        $grid = $this->board->grid;
        $max = 0;
        $max_coord = [0,0];
        
        for ($i = 0; $i < BOARD_SIZE; $i++) {
            for ($j = 0; $j < BOARD_SIZE; $j++) {
                $streak = $this->vertStreak($i, $j, $user, $grid);
                if ($streak > $max) {
                    $max = $streak;
                    $max_coord = [$i,$j]; //x,y
                }
            }
        }
        
        return array ("coords" => $max_coord, "length" => $max);
    }
    function vertStreak($x, $y, $user, $grid) {
        $sum = 0;
        if ($x < BOARD_SIZE && $x >= 0 && $y < BOARD_SIZE && $y >= 0) {
            if ($grid[$y][$x] == $user) { //remember its [y][x]
                return 1 + $this->vertStreak($x, ++$y, $user, $grid);
            }
        }
        return $sum;
    }
    
    
    //gets the largest diagonal right streak down to right of given coordinate
    function checkDRight($user) {
        $grid = $this->board->grid;
        $max = 0;
        $max_coord = [0,0];
        
        for ($i = 0; $i < BOARD_SIZE; $i++) {
            for ($j = 0; $j < BOARD_SIZE; $j++) {
                $streak = $this->diagRStreak($i, $j, $user, $grid);
                if ($streak > $max) {
                    $max = $streak;
                    $max_coord = [$i,$j]; //x,y
                }
            }
        }
        
        return array ("coords" => $max_coord, "length" => $max);
    }
    function diagRStreak($x, $y, $user, $grid) {
        $sum = 0;
        if ($x < BOARD_SIZE && $x >= 0 && $y < BOARD_SIZE && $y >= 0) {
            if ($grid[$y][$x] == $user) { //remember its [y][x]
                return 1 + $this->diagRStreak(++$x, ++$y, $user, $grid);
            }
        }
        return $sum;
    }
    
    
    //gets the largest diagonal left streak down to left of given coordinate
    function checkDLeft($user) {
        $grid = $this->board->grid;
        $max = 0;
        $max_coord = [0,0];
        
        for ($i = 0; $i < BOARD_SIZE; $i++) {
            for ($j = 0; $j < BOARD_SIZE; $j++) {
                $streak = $this->diagLStreak($i, $j, $user, $grid);
                if ($streak > $max) {
                    $max = $streak;
                    $max_coord = [$i,$j]; //x,y
                }
            }
        }
        return array ("coords" => $max_coord, "length" => $max);
    }
    function diagLStreak($x, $y, $user, $grid) {
        $sum = 0;
        if ($x < BOARD_SIZE && $x >= 0 && $y < BOARD_SIZE && $y >= 0) {
            if ($grid[$y][$x] == $user) { //remember its [y][x]
                return 1 + $this->diagLStreak(--$x, ++$y, $user, $grid);
            }
        }
        return $sum;
    }
    
    
    
    function getWinRow($longest_data) {
        $result = [];
        $type = $longest_data["type"];
        $coords = $longest_data["coords"];
        $x = $coords[0];
        $y = $coords[1];
        
        if ($type == HORIZONTAL) {
            for ($i = 0; $i < 5; $i++) {
                $result[] = $x;
                $result[] = $y;
                $x++;  
            }
        } else if ($type == VERTICAL) {
            for ($i = 0; $i < 5; $i++) {
                $result[] = $x;
                $result[] = $y;
                $y++;  
            }
        } else if ($type == DIAGONAL_R) {
            for ($i = 0; $i < 5; $i++) {
                $result[] = $x;
                $result[] = $y;
                $x++; 
                $y++;
            }
        } else { // DIAGONAL_L
            for ($i = 0; $i < 5; $i++) {
                $result[] = $x;
                $result[] = $y;
                $x--;  
                $y++;
            }
        }
        
        return $result;
    }
    
    // updates the current instance's draw value if a draw is found
    function checkDraw() {
        $draw = true;
        for ($i = 0; $i < BOARD_SIZE && $draw; $i++) {
            for ($j = 0; $j < BOARD_SIZE && $draw; $j++) {
                if ($this->board->grid[$i][$j] == 0) {
                    $draw = false;
                    return;
                }
            }
        }
        $this->isDraw = true;
    }
    
    
}

?>