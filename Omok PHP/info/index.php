<?php // info/index.php
// Daniel Duru Section 2


require_once '../common/constants.php';

$data = array("size" => BOARD_SIZE, "strategies" => STRATEGIES);
echo json_encode($data);

?>
