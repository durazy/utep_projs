
<?php
// Daniel Duru Section 2

// constants for use throughout the project

// game constants
define('BOARD_SIZE', 15);
define('STRATEGIES', [
    "Smart",
    "Random" ]);
$strategies = array(
    "Smart" => "SmartStrategy",
    "Random" => "RandomStrategy" );
define('STRAT_CLASSES', $strategies);

// constants for files
define('DATA_DIR', "../writable/");
define('DATA_EXT', ".txt");

// constants for Board
define('PLAYER', 1);
define('CPU', 2);

//constants for direction
define('HORIZONTAL', 1);
define('VERTICAL', 2);
define('DIAGONAL_R', 3);
define('DIAGONAL_L', 4);
