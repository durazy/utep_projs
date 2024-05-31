<?php // new/index.php
// Daniel Duru Section 2

require_once '../common/constants.php';
require_once '../play/Game.php';
require_once '../common/utils.php';

define('STRATEGY', 'strategy'); // constant

// check if a strategy is provided
if (!array_key_exists(STRATEGY, $_GET)) { 
    echo createResponse("Strategy not specified");
    exit; 
}

$strategy = $_GET[STRATEGY];

/*
 * if the strategy is not one of the registered ones, print an error message. 
 * create a file to store the game data 
 * the name of the file is the generated pid
 */

if ($strategy == STRATEGIES[0] || $strategy == STRATEGIES[1]) {
    
    $pid = uniqid();
    $output = array("response" => true, "pid" => $pid);
    echo json_encode($output);
    
    //create file
    $filename = DATA_DIR . $pid . DATA_EXT;
    $strategy_type = STRAT_CLASSES[$strategy];
    $strategy = new $strategy_type();
    $game = new Game(BOARD_SIZE, $strategy);
    $data = $game->toJson();
    storeState($filename, $data);
 
} else {
    
    createResponse("Unknown strategy");
    
}

