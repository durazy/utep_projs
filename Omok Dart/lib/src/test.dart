import 'dart:io';

class Awesome {
  var worker = new Model();

  @override
  dynamic noSuchMethod(Invocation invocation) {
    if (invocation.isMethod && invocation.memberName == #model) {
      return worker.work();
    }
  }
}

class Model {
  int _x = 1;
  int get x {
    return _x * 2;
  }

  void work() {
    print("Working");
  }

  void yell(String m) {
    print("AAAAAA $m");
  }
}

void main() {
  int size = 5;
  var grid = List<dynamic>.generate(size, (index) => List<int>.generate(size, (index) => 0));
  print(grid);
}
