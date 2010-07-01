○Firebugmonekyについて
Firebugでgreasemonkeyスクリプトのデバッグが可能になるFirebugの拡張です。
Firefox3.5, Firefox3.6とFirebug1.5.4で動作します。

FirebugmonekyはgreasemonkeyAPIを(不完全ですが)自前で用意しているため
greasemonkeyをインストールしないでも動作します。

greasemonkeyAPIの実装のために以下のgreasemonkeyのソースを使用させて頂いています。
greasemonkey-0.8.20090123.1-fx Copyright 2004-2007 Aaron Boodman


○履歴
2010/07/01 0.2.0
・Firefox3.5, Firefox3.6に対応
・GM_deleteValue, GM_listValues, GM_registerMenuCommand追加(GM_registerMenuCommandはダミー)
・設定ファイルをxmlからjsonに変更
・E4Xの変数が存在するとブレークポイントを使用したデバッグができないため
  E4Xの変数を表示上は文字列として処理するようにした

2009/09/18 0.1.2
・余計なコードが混じっていたので削除
・readme_jp.txtの記述のミスを修正
・Firebugmonkeyを有効にしたときに自動的に「クロームのソースを表示」にチェックをいれるようにした
  (チェックが入っていないと動作しなかったのに、ドキュメントに記載し忘れていた)

2009/09/17 0.1.1
・ファイルエンコード処理が間違っていて、エラーになっていたのでその箇所を削除

2009/08/12 0.1.0
・公開