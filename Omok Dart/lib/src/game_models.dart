import 'dart:io';

class Game {
  final game = 0;
  String strategy = '';
}

class Board {
  final int _size;
  late List<List<String>> grid;

  int get size => _size;

  ///Constructs a board with a given size and a grid of size x size
  Board(this._size) {
    grid = List<List<String>>.generate(
        _size, (index) => List<String>.generate(_size, (index) => "."));
  }

  ///Updates the given coordinate with the player's stone
  void updateBoard(int x, int y, String stone) {
    grid[y][x] = stone;
  }

  ///Displays the current Board object
  void showBoard() {
    print("x 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5");
    print("y ------------------------------");

    for (int i = 0; i < _size; i++) {
      stdout.write("${(i + 1) % 10}| ");
      for (int j = 0; j < _size; j++) {
        stdout.write("${grid[i][j]} ");
      }
      stdout.write("\n");
    }
  }

  updateWinning(List<int> winningRow, String stone) {

  }
}

class Player {
  final String stone;

  Player(this.stone);
}
