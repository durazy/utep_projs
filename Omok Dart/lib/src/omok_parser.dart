import 'package:http/http.dart' as http;
import 'dart:convert';

class ResponseParser {
  ///Parse the response of the info page from the given url
  Future<Map> parseInfo(String urlFrom) async {
    String urlToUse = urlFrom + "/info";
    var url = Uri.parse(urlToUse);
    var response = await http.get(url);
    var info = json.decode(response.body);

    return info;
  }

  ///Parse the response of the new page from the given url
  Future<Map> parseNew(String urlFrom, String strat) async {
    String urlToUse = urlFrom + "/new?strategy=$strat";
    var url = Uri.parse(urlToUse);
    var response = await http.get(url);
    var info = json.decode(response.body);
    return info;
  }

  String getPid(Map info) {
    String pid;
    try {
      pid = info['pid'];
    } catch (e) {
      throw Exception("Response is not well formed");
    }
    return pid;
  }

  Future<Map> parsePlay(String urlFrom, String pid, List<int> move) async {
    int x = move[0] - 1;
    int y = move[1] - 1;
    String urlToUse = urlFrom + "/play?pid=$pid&x=$x&y=$y";
    var url = Uri.parse(urlToUse);
    var response = await http.get(url);
    var info = json.decode(response.body);
    return info;
  }
}
