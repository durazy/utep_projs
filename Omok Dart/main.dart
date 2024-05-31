import 'dart:io';
import 'package:omok_proj/src/omok_parser.dart';

import 'lib/src/console_ui.dart';
import 'lib/src/game_models.dart';
import 'lib/src/constants.dart' as constants;

///Omok Controller
void main() async {
  ConsoleUI console = ConsoleUI();
  print("Welcome to Omok game!");

  var gameInfo = await initiateGame(console);
  var gameData = gameInfo[0];
  var gameUrl = gameInfo[1];

  stdout.write("\nCreating a new game ......");

  var chosenStrategy = console.getStrat(gameData);
  String pid = await newGame(gameUrl, chosenStrategy);

  runGame(console, pid, gameUrl);
}

///Initiates game using a response from the /info page
Future<List> initiateGame(ConsoleUI console) async {
  String defaultUrl = constants.DEFAULT_URL;
  stdout.write('Enter the server URL [default: $defaultUrl] ');
  var info = await console.getGameInfo(defaultUrl);
  return info;
}

///Initialize game and return player id (/new page)
Future<String> newGame(String url, String strat) async {
  ResponseParser retriever = ResponseParser();
  var info = await retriever.parseNew(url, strat);
  return retriever.getPid(info);
}

///Run the game until end (/play page)
void runGame(ConsoleUI console, String pid, String gameUrl) async {
  String player = constants.PLAYER_STONE;
  String cpu = constants.CPU_STONE;
  String lastMove = constants.PLAYED_STONE;
  List<int> lastCoord = [0, 0];
  bool moved = false;
  Board gameBoard = Board(15);
  bool gameOver = false;
  ResponseParser retriever = ResponseParser();

  while (!gameOver) {
    gameBoard.showBoard();
    print("Player: $player, Server: $cpu (and $lastMove)");
    var move = console.getMove(gameBoard);
    var moveInfo = await retriever.parsePlay(gameUrl, pid, move);

    var playerMove = moveInfo['ack_move'];
    gameBoard.updateBoard(playerMove['x'], playerMove['y'], player);

    if (!playerMove['isWin'] && !playerMove['isDraw']) {
      //cpu move
      var cpuMove = moveInfo['move'];
      gameBoard.updateBoard(cpuMove['x'], cpuMove['y'], lastMove);

      if (moved) {
        gameBoard.updateBoard(lastCoord[0], lastCoord[1], cpu);
      }
      lastCoord = [cpuMove['x'], cpuMove['y']];
      moved = true;

      if (cpuMove['isWin']) {
        endGame(cpuMove, "!", gameBoard);
        gameOver = true;
      }
    } else {
      if (playerMove['isWin']) {
        endGame(playerMove, "!", gameBoard);
        print("Player wins!");
      } else {
        print("Draw!");
      }
      gameOver = true;
    }
  }
}

///Updates the board to show the winning row
void endGame(Map moveInfo, String stone, Board gameBoard) {
  var winRow = moveInfo['row'];
  for (int i = 0; i < 10; i += 2) {
    int x = winRow[i];
    int y = winRow[i + 1];
    gameBoard.updateBoard(x, y, stone);
  }
  gameBoard.showBoard();
}
