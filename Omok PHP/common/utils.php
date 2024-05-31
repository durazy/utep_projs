<?php
// Daniel Duru Section 2

// returns a json of a false response with a given reason
function createResponse($reason) {
    $data = array("response" => false, "reason" => $reason);
    return json_encode($data);
}

// store the current state of game into the given file
function storeState($filename, $data){
    $data = json_encode($data);
    file_put_contents($filename, $data);
}

// returns the current game state of a given data file as a json encoded string
function getState($filename) {
    return file_get_contents($filename);
}