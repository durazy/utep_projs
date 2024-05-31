<?php  
// Daniel Duru Section 2

require_once 'MoveStrategy.php';
require_once 'Board.php';

class RandomStrategy extends MoveStrategy {

    //chooses a random empty place on the board and returns it
	function pickPlace() {
	   
	    $size = $this->board->getSize();
	    $empty = false;
	    $place = null;
	    
	    while (!$empty) {
	        $x = rand(0, $size - 1);
	        $y = rand(0, $size - 1);
	        if ($this->board->getPiece($x,$y) == 0) {
	            $empty = true;
	            $place = [$x, $y];
	        }
	    }
		
	    return $place; 

	}
}

?>