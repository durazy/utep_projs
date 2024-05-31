<?php 
// Daniel Duru Section 2

require_once '../common/constants.php';
require_once 'Board.php';
require_once 'RandomStrategy.php';
require_once 'SmartStrategy.php';
require_once 'Move.php';

class Game {
    
    public $board; // the board where the pieces will be placed
    public $strategy; // the player's chosen strategy
    
    //set fields manually or they will be null
    function __construct($size = null, $strategy = null) {
        $this->board = new Board($size);
        $this->strategy = $strategy;
    }
    
    // returns the array of Class to be encoded in Json
    function toJson() {
        $board = $this->board->toJson();
        $strategy = $this->strategy->toJson();
        $data = array("strategy" => $strategy, "board" => $board);
        return $data;
    }
    
    // returns an instance of Game using a Json
    static function fromJson($json) {
        $obj = json_decode($json);
        $strategy = $obj->{'strategy'}; //array
        $board = $obj->{'board'}; //array
        $game = new Game();
        $game->board = Board::fromJson($board);
        $name = $strategy->{'name'};
        $game->strategy = $name::getStrat($game->board);
        return $game;
    }
    
    //processes the player's move
    //board is updated in the Move Class
    function makePlayerMove($x, $y) {
        $move = new Move($x, $y, $this->board, PLAYER);
        return $move;
    }
    
    //processes the computer's move
    //board is updated in the Move Class
    function makeOppMove() {
        $strat_type = get_class($this->strategy);
        $strat = new $strat_type($this->board);
        $coord = $strat->pickPlace();
        $move = new Move($coord[0], $coord[1], $this->board, CPU);
        return $move;
    }
}

?>