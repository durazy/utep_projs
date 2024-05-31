<?php
// Daniel Duru Section 2

require_once '../common/constants.php';
require_once '../common/utils.php';

class Board {
    
    public $grid; // the place of all the pieces
    private $size; // the width and length of the grid. default is 15

    // constructor
    function __construct($size = 15, $grid = null) {
        $this->size = $size;
        if ($grid == null) {
            for ($x = 0; $x < $size; $x ++) {
                $this->grid[] = array_fill(0, $size, 0);
            }
        } else {
            $this->grid = $grid;
        }
        
    }
    
    // returns the array of Class to be encoded in Json
    function toJson() {
        return array("size" => $this->size, "places" => $this->grid);
    }
    
    // returns a Board object using a Json decoded array
    static function fromJson($obj){
        $size = $obj->{'size'};
        $grid = $obj->{'places'};
        $board = new Board($size, $grid);
        return $board;
    }
    
    /*
     * 0 is an empty spot
     * 1 is a User spot
     * 2 is a Computer spot
     * assumes the passed x and y values are valid
     */
    function placePiece($x, $y, $player) {
        $this->grid[$y][$x] = $player;
    }
    
    // checks if the given x,y coordinates are valid and returns true if so
    function isValid($x, $y) {
        if (!($x < $this->size && $x > -1)) {
            echo createResponse("Invalid x coordinate, " . $x);
            return false;
        } 
        
        if (!($y < $this->size && $y > -1)) {
            echo createResponse("Invalid y coordinate, " . $y);
            return false;
        } 
        
        if (!($this->isEmpty($x,$y))) {
            echo createResponse("Coordinate is not empty");
            return false;
        }
        
        return true;
        
    }
    
    // returns the value of the place at the coordinates
    // assumes the coordinates have already been checked
    function getPiece($x, $y) {
            return $this->grid[$y][$x];
    }
    
    // checks if a piece has already been placed in given spot
    function isEmpty($x, $y) {
        return $this->getPiece($x, $y) == 0;
    }
    
    // returns size of grid (just one dimension)
    function getSize() {
        return $this->size;
    }
    
    //checks if x and y is on board and empty
    function canChoose($x, $y) {
        $onBoard = $x < $this->size && $x > -1 && $y < $this->size && $y > -1;
        $empty = $this->isEmpty($x, $y);
        return $onBoard && $empty;
    }
}

?>