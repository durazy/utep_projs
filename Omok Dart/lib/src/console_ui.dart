import 'dart:io';
import 'omok_parser.dart';
import 'game_models.dart';

class ConsoleUI {
  ///Get the url to start the game and the strategy to run the game
  Future<List> getGameInfo(String defaultUrl) async {
    String? url = stdin.readLineSync()!;
    if (url.length < 1) {
      url = defaultUrl;
    }

    stdout.write("Obtaining server information ......");
    ResponseParser retrieve = ResponseParser();
    var info = await retrieve.parseInfo(url);

    return [info, url];
  }

  ///Prompts the user to choose a strategy and returns said choice
  String getStrat(info) {
    var strats = info['strategies'];
    var strat1 = strats[0];
    var strat2 = strats[1];
    int defaultMode = 1;

    bool valid = false;
    int choice = -1;

    while (!valid) {
      stdout.write(
          "\nSelect the server strategy: 1. $strat1 2. $strat2 [default: $defaultMode] ");
      var line = stdin.readLineSync();

      try {
        var selection = int.parse(line.toString());
        if (selection == 1 || selection == 2) {
          valid = true;
        } else {
          stdout.write("\nPlease enter a valid strategy");
        }
      } on FormatException {
        stdout.write("Invalid item passed");
      }
    }

    if (choice == 1) {
      return strat1;
    }
    return strat2;
  }

  ///Prompts the user for a move and returns the indices x,y
  List<int> getMove(Board gameBoard) {
    int size = gameBoard.size;
    bool valid = false;
    var values = [-1, -1];
    while (!valid) {
      stdout.write("Enter x and y (1-15, e.g., 8 10): ");
      var line = stdin.readLineSync();

      try {
        var indices = [];
        line == null ? throw Exception : {indices = line.split(' ')};
        int index1 = values[0];
        int index2 = values[1];
        indices.length != 2
            ? throw Exception
            : {index1 = int.parse(indices[0])};
        index2 = int.parse(indices[1]);
        if (!((index1 >= 1) &
            (index1 < size + 1) &
            (index2 >= 1) &
            (index2 < size + 1))) {
          throw Exception;
        }
        values = [index1, index2];
        valid = true;
      } catch (e) {
        print("Invalid index!");
      }

      int x = values[0];
      int y = values[1];
      if (valid && gameBoard.grid[y - 1][x - 1] != ".") {
        valid = false;
        print("Not empty");
      }
    }
    return values;
  }
}
