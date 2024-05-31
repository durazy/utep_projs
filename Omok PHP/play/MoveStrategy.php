<?php
// Daniel Duru Section 2

require_once '../common/constants.php';

abstract class MoveStrategy {
   protected $board;

   //constructor
   function __construct(Board $board = null) {
      $this->board = $board;
   }

   abstract function pickPlace();

   // returns the array of Class to be encoded in Json
   function toJson() {
      return array("name" => get_class($this));
   }

   // creates an object of the current level of inheritance with current game board
   static function getStrat($board) {
       $strategy = new static($board);
       return $strategy;
   }
}
?>