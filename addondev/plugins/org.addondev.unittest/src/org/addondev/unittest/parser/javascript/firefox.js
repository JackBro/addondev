var document = 
{	

};


var gBrowser = {

	//プロパティ
	//XUL 要素からの継承
	align:null, 
	allowEvents:null, 
	boxObject:null, 
	builder:null, 
	className:null, 
	collapsed:null, 
	contextMenu:null, 
	controllers:null, 
	database:null, 
	datasources:null, 
	dir:null, 
	flex:null, 
	height:null, 
	hidden:null, 
	id:null, 
	left:null, 
	maxHeight:null, 
	maxWidth:null, 
	menu:null, 
	minHeight:null, 
	minWidth:null, 
	observes:null, 
	ordinal:null, 
	orient:null, 
	pack:null, 
	persist:null, 
	ref:null, 
	resource:null, 
	statusText:null, 
	style:null, 
	tooltip:null, 
	tooltipText:null, 
	top:null, 
	width:0,
	
	//DOM 要素からの継承
	attributes :null, 
	baseURI :null, 
	childElementCount :null, 
	childNodes :null, 
	children :null, 
	clientHeight :null, 
	clientLeft :null, 
	clientTop :null, 
	clientWidth :null, 
	firstChild :null, 
	firstElementChild :null, 
	lastChild :null, 
	lastElementChild :null, 
	localName :null, 
	namespaceURI :null, 
	nextElementSibling :null, 
	nextSibling :null, 
	nodeName :null, 
	nodeType :null, 
	nodeValue :null, 
	ownerDocument :null, 
	parentNode :null, 
	prefix :null, 
	previousElementSibling :null, 
	previousSibling :null, 
	scrollHeight :null, 
	scrollLeft :null, 
	scrollTop :null, 
	scrollWidth :null, 
	tagName :null, 
	textContent:null,
	
	/**
	 *	
	 *  tabbrowser 内の browser 要素のリストを保持します。 
	 * 	型: browser 要素のノードリスト 
	 */
	browsers;,

	/**
	 *	@type Boolean
	 *   型: 論理型 
	 *   セッション履歴で前のページに移動できるとき、このプロパティは true になり、Back ボタンが有効になります。このプロパティは読み取り専用です。 
	 */
	canGoBack:,

	/**
	 *	@type Boolean
	    型: 論理型 
	    セッション履歴で次のページに移動できるとき、このプロパティは true になり、Forward ボタンが有効になります。
	    このプロパティは読み取り専用です。 
	 */
	canGoForward:,

	/**
	    型: document 
	    この読み取り専用のプロパティは要素内の document オブジェクトを含みます。 
	 */
	contentDocument:,

	/**
	    型: 文字列型 
	    この読み取り専用のプロパティは browser 内の document オブジェクトの title を含みます。 
	 */
	contentTitle:,

	/**
	    型: nsIContentViewerEdit 
	    この読み取り専用のプロパティは、document 上のクリップボード操作を扱う nsIContentViewerEdit を含みます。 
	 */
	contentViewerEdit:,

	/**
	    型: nsIContentViewerEdit 
	    この読み取り専用のプロパティは、document 上のクリップボード操作を扱う nsIContentViewerEdit を含みます。 
	 */
	contentViewerFile:,
	
	/**
	    型: window
	    この読み取り専用のプロパティは要素内の window オブジェクトを含みます。
	 */
	contentWindow:,

	/**
	    型: nsIURI 
	    この読み取り専用のプロパティは現在読み込まれている URL を含みます。URL を変更するには loadURI() メソッドを使用してください。
	 */
	currentURI:,
 
	/**
	    型: nsIDocShell 
	    この読み取り専用のプロパティは document の nsIDocShell オブジェクトを含みます。
	 */
	docShell:,
 
	/**
	    型: nsIDocumentCharsetInfo 
	    この読み取り専用のプロパティは document の nsIDocumentCharsetInfo オブジェクトを含みます。このオブジェクトは document の表示に使用される文字セットを扱うために使用します。
	 */
	documentCharsetInfo:,
 
	/**
	    型: ホームページ URL の文字列 
	    このプロパティはユーザのホームページ設定の値を保持します。 
	 */
	homePage:,

	/**
	    型: nsIMarkupDocumentViewer 
	    この読み取り専用のプロパティは、document を描画する nsIMarkupDocumentViewer を含みます。 
	 */
	markupDocumentViewer:,

	/**
	    型: nsISecureBrowserUI 
	    この読み取り専用のプロパティは、読み込まれた document のセキュリティレベルを決定するオブジェクトを含みます。 
	 */
	securityUI:,

	/**
	    型: browser 要素 
	    この読み取り専用のプロパティは、現在表示されている browser 要素を返します。 
	 */
	selectedBrowser:,

	/**
	    型: tab 要素 
	    現在選択されたタブへの参照。これは常に tabs 要素内の tab 要素の一つになります。現在選択されたタブを変更するには、このプロパティに値を割り当ててください。 

	 */
	selectedTab:,
	
	/**
	    型: nsISHistory 
	    この読み取り専用のプロパティは、セッション履歴を保持する nsISHistory オブジェクトを含みます。
	 */
	sessionHistory:,
 
	/**
	    型: tabs 要素 
	    tab を含む tabs 要素を返します。 
	 */
	tabContainer:,

	/**
	    型: nsIWebBrowserFind 
	    この読み取り専用のプロパティは、document 内のテキストの検索に使用することができる nsIWebBrowserFind オブジェクトを含みます。 
	 */
	webBrowserFind:,

	
	/**
	    型: nsIWebNavigation 
	    この読み取り専用のプロパティは document の nsIWebNavigation オブジェクトを含みます。このメソッドのほとんどは、goBack() や goForward() のように要素自身から直接呼び出せます。また、reloadWithFlags() および loadURIWithFlags() によって使用される読み込み定数を含みます。 
	 */
	webNavigation:,
 
	
	/**
	 * 
	 *   型: nsIWebProgress 
	 *   この読み取り専用のプロパティは、document 読み込みの進捗を監視する nsIWebProgress オブジェクトを含みます。 
	 */
	webProgress:,

	
	//メソッド
	//XUL 要素からの継承
	blur:function(), 
	click:function(), 
	doCommand:function(), 
	focus:function(), 
	getElementsByAttribute:function(),
	getElementsByAttributeNS:function(),
	
	//DOM 要素からの継承
	addEventListener:null, 
	appendChild:null, 
	compareDocumentPosition:null, 
	dispatchEvent:null, 
	getAttribute:null, 
	getAttributeNode:null, 
	getAttributeNodeNS:null, 
	getAttributeNS:null, 
	getElementsByTagName:null, 
	getElementsByTagNameNS:null, 
	getFeature:null, 
	getUserData:null, 
	hasAttribute:null, 
	hasAttributeNS:null, 
	hasAttributes:null, 
	hasChildNodes:null, 
	insertBefore:null, 
	isEqualNode:null, 
	isSameNode:null, 
	isSupported:null, 
	lookupNamespaceURI:null, 
	lookupPrefix,
	normalize:null, 
	removeAttribute:null, 
	removeAttributeNode:null, 
	removeAttributeNS:null, 
	removeChild:null, 
	removeEventListener:null, 
	replaceChild:null, 
	setAttribute:null, 
	setAttributeNode:null, 
	setAttributeNodeNS:null, 
	setAttributeNS:null, 
	setUserData:null, 
	
	
	/**
	 *   戻り値の型: 戻り値なし 
	 *   読み込まれた document を監視する進捗リスナーを browser に追加します。進捗リスナーは nsIWebProgressListener インタフェースを実装しなければなりません。
	 * 
	 * 	
	 */
	addProgressListener:function( listener ){},
 
	
	/**
	 *   戻り値の型: tab 要素 
	 *   指定した URL のページを読み込む新しいタブを開きます。残りの引数は任意です。必要に応じてタブバーが表示されます。 
	 *   例は Code snippets:Tabbed browser をご覧ください。 
	 *   文字列から postData を準備するには Preprocessing POST Data をご覧ください。
	 */
	addTab:function( URL, referrerURI, charset, postData, owner, allowThirdPartyFixup ){},
 
	
	/**
	    戻り値の型: 戻り値なし 
	    Firefox では使用不可 
	    いくつかの新しいタブを既存のタブの次に追加します。
	    引数は、タブに読み込む各ドキュメントオブジェクトの配列です。
	    オブジェクトはスクリプトで定義され、読み込むページの URL のURI プロパティを含みます。
	    referrerURI プロパティは、リファラページを設定するために任意で使用されます。
	 */
	appendGroup:function( group ){},
 
	/**
	    型: browser 要素 
	    指定した tab index の位置の browser を返します。 
	 */
	getBrowserAtIndex:function( index ){},

	/**
	    戻り値の型: 整数型 
	    指定した document の browser の index を返します。
	 */
	getBrowserIndexForDocument:function( document ){},
 
	/**
	    型: browser 要素 
	    指定した document の browser を返します。 
	 */
	getBrowserForDocument:function( document ){},

	/**
	    型: browser 要素 
	    指定した tab 要素の browser を返します。 
	 */
	getBrowserForTab:function( tab ){},

	/**
	    型: notificationbox 要素 
	    指定した browser 要素の notificationbox を返します。
	 */
	getNotificationBox:function( browser ){},
 
	/**
	    戻り値の型: 戻り値なし 
	    履歴内のページを一つ戻ります。 
	 */
	goBack:function(){},

	/**
	    Firefox では使用不可 
	    戻り値の型: 戻り値なし 
	    前のタブグループへ戻ります。 
	 */
	goBackGroup:function(){},

	/**
	    戻り値の型: 戻り値なし 
	    履歴内のページを一つ進みます。
	 */
	goForward:function(){},
 
	/**
	    Firefox では使用不可 
	    戻り値の型: 戻り値なし 
	    次のタブグループへ進みます。 
	 */
	goForwardGroup:function(){},

	/**
	    戻り値の型: 戻り値なし 
	    ユーザのホームページを browser に読み込みます。 
	 */
	goHome:function(){},

	/**
	    戻り値の型: 戻り値なし 
	    与えられた index を持つ履歴内のページへ移動します。先へ進むには正の整数、前へ戻るには負の整数を使用します。 
	 */
	gotoIndex:function( index ){},

	/**
	    Not in Firefox 
	    戻り値の型: 最初の tab 
	    ページのグループを複数のタブに読み込みます。これらは browser.tabs.loadGroup 設定の状態によって、それぞれ追加、または置き換えられます。引数は、タブに読み込む各ドキュメントオブジェクトの配列です。オブジェクトはスクリプトで定義され、読み込むページの URL の URI プロパティを含みます。referrerURI プロパティは、リファラページを設定するために任意で使用されます。この関数は最初に読み込まれたタブへの参照を返します。 

	 */
	loadGroup:function( group ){},
	
	/**
	    戻り値の型: tab 要素 
	    指定した URL のページを読み込む新しいタブを開きます。残りの引数は任意です。このメソッドは addTab と同じ動作をしますが、loadInBackground 引数で新しいタブを前面と背面のどちらに開くかを選ぶことができます。また、owner タブは自動的に指定されるため、owner 引数はありません。 

	 */
	loadOneTab:function( URL, referrerURI, charset, postData, loadInBackground, allowThirdPartyFixup ){},
	
	/**
	    戻り値の型: 戻り値なし 
	    uris 配列で指定された URI のセットをタブに読み込みます。loadInBackground が true の場合、これらのタブは背面に読み込まれます。replace が true の場合、タブを追加する代わりに、現在表示されているタブが指定した URI に置き換えられます。 

	 */
	loadTabs:function( uris, loadInBackground, replace ){},
	
	/**
	    戻り値の型: 戻り値なし 
	    与えられた referrer と charset で URL を document に読み込みます。 
	 */
	loadURI:function( uri, referrer, charset ){},

	/**
	    戻り値の型: 戻り値なし 
	    引数に指定した読み込みフラグ(flags) および与えられたリファラ(referrer)、文字セット(charset)、POST データで URL を document に読み込みます。reloadWithFlags() メソッドで許可されたフラグに加え、次のフラグも有効です。 
	
	    * LOAD_FLAGS_IS_REFRESH: このフラグは、meta タグの refresh や redirect によって URL が読み込まれたときに使用されます。
	    * LOAD_FLAGS_IS_LINK: このフラグは、ユーザがリンクをクリックして URL が読み込まれたときに使用されます。これに応じて HTTP Referer ヘッダが設定されます。
	    * LOAD_FLAGS_BYPASS_HISTORY: URL をセッション履歴に追加しません。
	    * LOAD_FLAGS_REPLACE_HISTORY: セッション履歴内の現在の URL を新しいものと置き換えます。このフラグはリダイレクトに使用されます。
	
	(referrer および postData 引数の詳細は nsIWebNavigation.loadURI() をご覧ください。)
	 */
	loadURIWithFlags:function( uri, flags, referrer, charset, postData ){},

	/**
	    戻り値の型: 戻り値なし 
	    browser 内の document を再度読み込みます。  
	 */
	reload:function(){},

	/**
	    戻り値の型: 戻り値なし 
	    すべてのタブのコンテンツを再度読み込みます。
	 */
	reloadAllTabs:function(){},
 
	/**
	    戻り値の型: 戻り値なし 
	    指定したタブのコンテンツを再度読み込みます。 
	 */
	reloadTab:function( tab ){},

	/**
	    戻り値の型: 戻り値なし 
	    browser 内の document を与えられた読み込みフラグ(flags) で再度読み込みます。下記のフラグが使用されます。これらはすべて webNavigation プロパティ (または nsIWebNavigation インタフェース) の定数です。記号( | ) を使用してフラグを組み合わせることができます。 
	
	    * LOAD_FLAGS_NONE: 特別なフラグなし。document は普通に読み込まれます。
	    * LOAD_FLAGS_BYPASS_CACHE: ページを再度読み込みます。ページがキャッシュ内にあっても無視されます。このフラグは reload ボタンが Shift キーを押しながら押下されたときに使用されます。
	    * LOAD_FLAGS_BYPASS_PROXY: プロクシサーバを無視してページを再度読み込みます。
	    * LOAD_FLAGS_CHARSET_CHANGE: このフラグは、文字セットが変更されたため document を再度読み込む必要がある場合に使用されます。
	 */
	reloadWithFlags:function( flags ){},

	/**
	    戻り値の型: 戻り値なし 
	    指定したタブを除くすべてのタブパネルを削除します。一つのタブページのみが表示されている場合、このメソッドは何もしません。 
	 */
	removeAllTabsBut:function( tabElement ){},

	/**
	    戻り値の型: tab 要素 
	    現在表示されているタブページを削除します。一つのタブのみが表示されている場合、このメソッドは何もしません。
	 */
	removeCurrentTab:function(){},
 
	/**
	    戻り値の型: 戻り値なし 
	    進捗リスナーを browser から削除します。 
	 */
	removeProgressListener:function( listener ){},

	/**
	    戻り値の型: 戻り値なし 
	    与えられた tab 要素に対応する特定のタブ化されたページを削除します。タブが 1 つしか表示されていない場合、このメソッドは何もしません。必要に応じて、タブが 1 つしかない場合はタブバーが折り畳まれます。 

	 */
	removeTab:function( tabElement ){},
	
	/**
	    Firefox では使用不可 
	    戻り値の型: セッション履歴オブジェクトの配列 
	    既存のタブを新しいセットで置き換えます。置き換え前のタブのほうが多い場合は、余りのタブは削除されません。先に既存のタブを削除して完全に置き換えるには removeTab() メソッドを使用してください。引数は読み込む各ドキュメントオブジェクトの配列です。オブジェクトはスクリプトで定義され、読み込むページの URL の URI プロパティを含みます。referrerURI プロパティは、リファラページを設定するために任意で使用されます。このメソッドは、削除されたタブのセッション履歴オブジェクトの配列を返します。 
 
	 */
	replaceGroup:function( group ){},
	
	/**
	    戻り値の型: 戻り値なし 
	    中止ボタンの押下と同じです。このメソッドは現在の document の読み込みを中止します。 
	 */
	stop:function(){}
};
