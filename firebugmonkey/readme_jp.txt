○Firebugmonekyについて
Firebugでgreasemonkeyスクリプトのデバッグが可能になるFirebugの拡張です。
Firefox3.5, Firefox3.6とFirebug1.5.4で動作します。

FirebugmonekyはgreasemonkeyAPIを(不完全ですが)自前で用意しているため
greasemonkeyをインストールしないでも動作します。

greasemonkeyAPIの実装のために以下のgreasemonkeyのソースを使用させて頂いています。
greasemonkey-0.8.20090123.1-fx Copyright 2004-2007 Aaron Boodman


○履歴
2010/08/05 0.3.0
・スクリプトの管理の「新規作成」で保存ダイアログを表示して任意の場所に保存できるようにした
・スクリプトの管理の「新規作成」で作成されるスクリプトはテンプレートを使用するようにした
・スクリプトの管理の「削除」で確認ダイアログを表示するかのチェックボックスを用意した
・スクリプトの管理のファイル表示をフルパス表示に変更
・スクリプトの管理に「ファイル選択」を追加
・スクリプトの管理に「フォルダを開く」を追加
・スクリプトの管理にテンプレートタブ追加
・編集やフォルダを開くとき、パスに非asciiが含まれているとパスが文字化けしていたのでパスをエンコードする設定を追加

2010/08/02 0.2.1
・スクリプトの有効/無効の判断にバグがあったので修正
・@requireや@resourceがhttp://example/updater.php?id=0とかの場合、ファイル名がupdater.php?id=0
になってしまい、ローカルへの保存に失敗していたので?を_に置換するようにした
・@requireや@resourceでファイルが作成されないうちにスクリプトを実行しようとしていたので
webからの取得を同期で行うようにした
・スクリプト管理画面で削除を行った時、ディレクトリごと削除するようにした
・firebugの設定変更にスクリプトパネルの有効化を追加

2010/07/05 0.2.0
・Firebug1.5.4(Firefox3.5, Firefox3.6)に対応
・GM_deleteValue, GM_listValues, GM_registerMenuCommand追加(GM_registerMenuCommandは中身が空のダミー)
・設定ファイルをxmlからjsonに変更
・E4Xの変数が存在するとブレークポイントを使用したデバッグができないため、E4Xの変数を表示上は文字列として処理するようにした
・スクリプトごとにフォルダを作成するようにした
・@require, @resourceで相対パス(例 ../test.png)を使用できるようにした
・firebugの設定が以下になっていない場合(Firebugmonekyのデバッグに必要)、ダイアログを表示してOKの場合に設定するようにした
  extensions.firebug.service.filterSystemURLs=false
  extensions.firebug.service.showAllSourceFiles=true

2009/09/18 0.1.2
・余計なコードが混じっていたので削除
・readme_jp.txtの記述のミスを修正
・Firebugmonkeyを有効にしたときに自動的に「クロームのソースを表示」にチェックをいれるようにした
  (チェックが入っていないと動作しなかったのに、ドキュメントに記載し忘れていた)

2009/09/17 0.1.1
・ファイルエンコード処理が間違っていて、エラーになっていたのでその箇所を削除

2009/08/12 0.1.0
・公開