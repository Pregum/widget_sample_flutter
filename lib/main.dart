import 'package:flutter/material.dart';
import 'package:home_widget/home_widget.dart';

const String countId = '_counter';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(const MyApp());
}

class MyApp extends StatelessWidget {
  const MyApp({Key? key}) : super(key: key);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Home widget sample',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      home: const MyHomePage(title: 'Home widget sample'),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key? key, required this.title}) : super(key: key);

  final String title;

  @override
  State<MyHomePage> createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  int _counter = 0;
  final String countId = '_counter';
  final defaultVal = -1;

  @override
  void initState() {
    super.initState();

    HomeWidget.widgetClicked.listen((Uri? uri) {
      loadData();
    });

    loadData();
  }

  Future<void> loadData() async {
    final val =
        await HomeWidget.getWidgetData(countId, defaultValue: defaultVal);

    setState(() {
      _counter = val!;
    });

    updateHomeWidget();
  }

  Future<void> updateHomeWidget() async {
    await HomeWidget.saveWidgetData(countId, _counter);
    await HomeWidget.updateWidget(
        name: 'HomeScreenWidgetProvider',
        androidName: 'HomeScreenWidgetProvider');
    // iosのextensionを更新する場合は以下のように指定する
    // await HomeWidget.updateWidget(iOSName: 'HomeScreenWidgetProvider');
  }

  void _incrementCounter() {
    setState(() {
      _counter++;
    });
    updateHomeWidget();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            const Text(
              'You have pushed the button this many times:',
            ),
            Text(
              '$_counter',
              style: Theme.of(context).textTheme.headline4,
            ),
          ],
        ),
      ),
      floatingActionButton: FloatingActionButton(
        onPressed: _incrementCounter,
        tooltip: 'Increment',
        child: const Icon(Icons.add),
      ), // This trailing comma makes auto-formatting nicer for build methods.
    );
  }
}
