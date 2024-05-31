<?php
// Daniel Duru Section 2

require_once 'MoveStrategy.php';
require_once 'Board.php';
require_once 'Move.php';
require_once '../common/constants.php';

class SmartStrategy extends MoveStrategy {
    
    // use move to pick a place
    // default to choosing the longest for themselves
    function pickPlace() {
        
        $size = $this->board->getSize();
        $place = null;
        $m = new Move(0, 0, $this->board, 0, true);
        $longest_data_player = $m->checkLongest(PLAYER);
        $chooseable = [];
        $chosen = false;

        // block the player's longest if it's 3 with two open places or 4 in a row
        if ($longest_data_player["length"] > 2) {
            $chooseable = $this->checkAroundLongest(PLAYER, $longest_data_player);
            if (count($chooseable) > 0) {
                $chosen = true;
                return $chooseable[0];
            }
        }
        
        // checks for current longest point of the computer
        $longest_data_cpu = $m->checkLongest(CPU); // has "type" => $longest_type, "coords" => $longest_start, "length" => $longest_length
        if (!$chosen & $longest_data_cpu["length"] > 0) {
            $chooseable = $this->checkAroundLongest(CPU, $longest_data_cpu);
            $chosen = count($chooseable) > 0;
            if ($chosen) {
                $chosen = true;
                return $chooseable[0];
            }
        } 
        
        
        // if no coordinate has been chosen at this point, choose random
        $empty = false;
        if (!$chosen) {
            while (!$empty) {
                $x = rand(0, $size - 1);
                $y = rand(0, $size - 1);
                if ($this->board->getPiece($x,$y) == 0) {
                    $empty = true;
                    $place = [$x, $y];
                }
            }
            
        }
        
        return $place; 
    }
    
    // checks the ends of the longest streak to generate places to put a piece
    function checkAroundLongest($user, $longest_data) {
        $result = [];
        $type = $longest_data["type"];
        
        if ($type == HORIZONTAL) {
            $result = $this->getHorz($user, $longest_data);
        } else if ($type == VERTICAL) {
            $result = $this->getVert($user, $longest_data);
        } else if ($type == DIAGONAL_R) {
            $result = $this->getRDiag($user, $longest_data);
        } else { // DIAGONAL_L
            $result = $this->getLDiag($user, $longest_data);
        }
        
        return $result;
    }
    
    //helpers to get the end points of a specified direction
    function getHorz($user, $longest_data) {
        $length = $longest_data["length"];
        $start = $longest_data["coords"];
        $result = [];
        
        $point1 = [$start[0] - 1, $start[1]];  //x,y
        $point2 = [$start[0] + $length, $start[1]];
        if ($this->board->canChoose($point1[0], $point1[1])) {
            $result[] = $point1;
        }
        if ($this->board->canChoose($point2[0], $point2[1])) {
            $result[] = $point2;
        }
        
        return $result;
    }
    function getVert($user, $longest_data) {
        $length = $longest_data["length"];
        $start = $longest_data["coords"];
        $result = [];
        
        $point1 = [$start[0], $start[1] - 1];  //x,y
        $point2 = [($start[0]), ($start[1] + $length)];
        
        if ($this->board->canChoose($point1[0], $point1[1])) {
            $result[] = $point1;
        }
        if ($this->board->canChoose($point2[0], $point2[1])) {
            $result[] = $point2;
        }
        
        return $result;
    }
    function getRDiag($user, $longest_data) {
        $length = $longest_data["length"];
        $start = $longest_data["coords"];
        $result = [];
        
        $point1 = [$start[0] - 1, $start[1] - 1];  //x,y
        $point2 = [($start[0] + $length), ($start[1] + $length)];
        
        if ($this->board->canChoose($point1[0], $point1[1])) {
            $result[] = $point1;
        }
        if ($this->board->canChoose($point2[0], $point2[1])) {
            $result[] = $point2;
        }
        
        return $result;
    }
    function getLDiag($user, $longest_data) {
        $length = $longest_data["length"];
        $start = $longest_data["coords"];
        $result = [];
        
        $point1 = [$start[0] + 1, $start[1] - 1];  //x,y
        $point2 = [($start[0] - $length), ($start[1] + $length)];
        
        if ($this->board->canChoose($point1[0], $point1[1])) {
            $result[] = $point1;
        }
        if ($this->board->canChoose($point2[0], $point2[1])) {
            $result[] = $point2;
        }
        
        return $result;
    }
    
}


