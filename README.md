# Robot Framework Remote Keyword Evaluator

This is an interactive GUI app for running your remote keywords, without having to put them inside a complete Robot Framework test and run it with pybot/jybot/standalone jar.

## Usage

If you've downloaded the EXE file, double-click it. If you've downloaded the standalone jar file, run `java -jar robot-remote-eval-x.x.x.jar` at your command prompt.

In the left text area, you type your commands (Robot Framework remote keywords plus args). One command per line, the first word(s) should be your Robot Framework keyword, followed by the args for that keyword separated by pipes `|` (just like you do in RIDE for the setup/teardown of a test).

On the right, you will see the results for each keyword printed out. Each set of keywords you run is separated by a row of asterisks `*`.

You can use the buttons at the bottom to clear each text area or save the contents to your computer's clipboard.

If you want to view a history of all the keywords you've run during a session, you can click the "Session History" button in the top-right of the GUI. You can keep this session history open and hit "Refresh" when you want to see more of your history.

## Caveat

This tool is not meant for running built-in Robot Framework keywords. It's meant solely as a tool for testing remote keywords on a small scale, so you can play with them in a more dynamic way than writing example Robot Framework tests just for the purposes of trying out new keywords.

## License

Copyright (C) 2010 Daniel L. Gregoire (semperos)

Distributed under the Eclipse Public License, the same as Clojure.
