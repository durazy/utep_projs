<?php // play/index.php
// Daniel Duru Section 2

require_once '../common/constants.php';
require_once '../common/utils.php';
require_once 'Game.php';
require_once 'Move.php';
require_once 'Board.php';

/*
 * error checking for valid game
 */ 
if (!array_key_exists('pid', $_GET)){
    echo createResponse("pid not specified");
    exit;
}

$pid = $_GET['pid']; // player id
$filename = DATA_DIR . $pid . DATA_EXT;

if (!file_exists($filename)){
    echo createResponse("unknown pid");
}


/*
 * error checking for valid moves and inputs
 */ 
if (!array_key_exists('x', $_GET)){
    echo createResponse("x not specified");
    exit;
}

if (!array_key_exists('y', $_GET)){
    echo createResponse("y not specified");
    exit;
}

$x = $_GET['x'];
$y = $_GET['y'];


$game_data = getState($filename);
$game = Game::fromJson($game_data);
$board = $game->board;

// can move to given x and y
if(!($board->isValid($x,$y))) {
    exit;
}


/*
 * process moves
 */

// player moves
$player_move = $game->makePlayerMove($x, $y);

if ($player_move->isWin || $player_move->isDraw) {
    echo moveResponse($player_move);
    exit;
}

// computer moves
$comp_move = $game->makeOppMove();
echo moveResponse($player_move, $comp_move);
if ($comp_move->isWin || $comp_move->isDraw) {
    exit;
}



// save data
$data = $game->toJson();
storeState($filename, $data);

// return move response as Json string
function moveResponse($player, $opp = null) {
    $player_move = $player->toJson();
    
    $result = array("response" => true, "ack_move" => $player_move);
    
    if ($opp) {
        $opp_move = $opp->toJson();
        $opp_move = $opp->toJson();
        $result["move"] = $opp_move;
    }
    
    return json_encode($result);
}

?>
