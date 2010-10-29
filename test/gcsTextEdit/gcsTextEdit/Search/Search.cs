using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace YYS
{

    //public class FindEventArgs : EventArgs {
    //    private Point location;
    //    public string Link { get; private set; }
    //    public MouseButtons Button { get; private set; }

    //    public FindEventArgs(DPos s, DPos e) {

    //    }
    //}
    //public class ReplaceEventArgs : EventArgs {
    //    private Point location;
    //    public string Link { get; private set; }
    //    public MouseButtons Button { get; private set; }

    //    public ReplaceEventArgs(DPos s, DPos e) {

    //    }
    //}

    //public delegate void FindEventHandler(Object sender, string line, int x, int y);
    //public delegate void ReplaceEventHandler(Object sender, string line, int x, int y);

    public class Search
    {
        private GCsTextEdit view;
        private Document document;
        public ISearch Searcher { get; set; }

        public string SearchWord;
        public string ReplaceWord;

        //public string searchstr;

        public Search(GCsTextEdit edit)
        {
            this.view = edit;
            this.document = this.view.Document;
        }

        public void FindPrevImpl(){
	        // カーソル位置取得
	        //VPos stt = new VPos();
            //VPos end = new VPos();
	        //view.cursor.getCurPos( out stt, out end );
            DPos stt, end;
            view.GetSelction(out stt, out end);

	        if( stt.ad!=0 || stt.tl!=0 ){
		        // 選択範囲先頭の１文字前から検索
		        DPos s;
		        if( stt.ad == 0 )
			        //s = new DPos( stt.tl-1, view.Document.len(stt.tl-1) );
                    s = new DPos( stt.tl-1, view.Document.GetLength(stt.tl-1) );
		        else
			        s = new DPos( stt.tl, stt.ad-1 );

		        // 検索
                DPos b = new DPos(); DPos e = new DPos();
		        if( FindPrevFromImpl( s, ref b, ref e ) ){
			        // 見つかったら選択
			        //view.cursor.MoveCur( b, false );
			        //view.cursor.MoveCur( e, true );
                    view.SetSelction(b, e);

			        return;
		        }
	        }

	        // 見つからなかった場合
	        //NotFound();
        }
        private bool FindPrevFromImpl( DPos s, ref DPos beg, ref DPos end ){
            Searcher.SearchWord = this.SearchWord;

	        // １行ずつサーチ
            //Document d = view.Document();
            //IDocument d = view.Document;
	        //for( int mbg=0,med=0; ; s.ad=d.len(--s.tl) ){
            for (int mbg = 0, med = 0; ; s.ad = document.GetLength(--s.tl)) {
		        if( Searcher.Search(
			        //d.tl(s.tl).ToString(), d.len(s.tl), s.ad, ref mbg, ref med ) ){
                    document.GetText(s.tl), document.GetLength(s.tl), s.ad, ref mbg, ref med)) {
			        beg.tl = end.tl = s.tl;
			        beg.ad = mbg;
			        end.ad = med;
			        return true; // 発見
		        }
		        if( s.tl==0 )
			        break;
	        }
	        return false;
        }

        public void FindNextImpl(){
	        // カーソル位置取得
	        //VPos stt, end;
	        //view.cursor.getCurPos( out stt, out end );
            DPos stt, end;
            view.GetSelction(out stt, out end);

	        // 選択範囲ありなら、選択範囲先頭の１文字先から検索
	        // そうでなければカーソル位置から検索
	        DPos s = new DPos(stt.tl, stt.ad);
	        if( stt != end )
                //if (stt.ad == view.Document.len(stt.tl))
                if (stt.ad == document.GetLength(stt.tl))
			        s = new DPos( stt.tl+1, 0 );
		        else
			        s = new DPos( stt.tl, stt.ad+1 );

	        // 検索
            DPos b = new DPos();
            DPos e = new DPos();
	        if( FindNextFromImpl( s, ref b, ref e ) ){
		        // 見つかったら選択
                //view.cursor.MoveCur(b, false);
                //view.cursor.MoveCur(e, true);
                view.SetSelction(b, e);
		        return;
	        }

	        // 見つからなかった場合
	        //NotFound();
        }

        private bool FindNextFromImpl(DPos s, ref DPos beg, ref DPos end) {
            Searcher.SearchWord = this.SearchWord;
	        // １行ずつサーチ
            //Document d = view.GetDocument();
	        //for( int mbg=0,med=0,e=d.tln(); s.tl<e; ++s.tl, s.ad=0 )
            for (int mbg = 0, med = 0, e = document.tln(); s.tl < e; ++s.tl, s.ad = 0)
                //if (Searcher.Search(d.tl(s.tl).ToString(), d.GetLength(s.tl), s.ad, ref mbg, ref med)) {
                if (Searcher.Search(document.GetText(s.tl), document.GetLength(s.tl), s.ad, ref mbg, ref med)) {
			        beg.tl = end.tl = s.tl;
			        beg.ad = mbg;
			        end.ad = med;
			        return true; // 発見
		        }
	        return false;
        }

        public void ReplaceImpl(){
	        // カーソル位置取得
	        //VPos stt = new VPos();
            //VPos end = new VPos();
	        //view.cursor.getCurPos( out stt, out end );
            DPos stt, end;
            view.GetSelction(out stt, out end);

	        // 選択範囲先頭から検索
            DPos b = new DPos();
            DPos e = new DPos();
	        if( FindNextFromImpl( stt, ref b, ref e ) )
		        if( e == (DPos)end ){
                    //string str = replStr_.ConvToWChar();
                    //int len = replstr.Length;

			        // 置換
                    //edit.getDoc().Execute( doc::Replace(
                    //    b, e, ustr, ulen
                    //) );

                    //view.Document.Execute(new Replace(b, e, ReplaceWord));
                    document.Replace(b, e, ReplaceWord);

                    if (FindNextFromImpl(new DPos(b.tl, b.ad + ReplaceWord.Length), ref b, ref e))
			        {
				        // 次を選択
				        //view.cursor.MoveCur( b, false );
				        //view.cursor.MoveCur( e, true );
                        view.SetSelction(b, e);
				        return;
			        }
		        }
		        else
		        {
			        // そうでなければとりあえず選択
			        //view.cursor.MoveCur( b, false );
			        //view.cursor.MoveCur( e, true );
                    view.SetSelction(b, e);
			        return;
		        }

	        // 見つからなかった場合
	        //NotFound();
        }

        public void ReplaceAllImpl(){
	        // まず、実行する置換を全てここに登録する
	        //doc::MacroCommand mcr;

	        // 置換後文字列
	        //const wchar_t* ustr = replStr_.ConvToWChar();
	        //const ulong ulen = my_lstrlenW( ustr );

            List<ICommand> mcr = new List<ICommand>();

	        // 文書の頭から検索
	        int dif=0;
            DPos s = new DPos(0, 0);//s(0,0)
            DPos b = new DPos();
            DPos e = new DPos();
	        while( FindNextFromImpl(new DPos(s), ref b, ref e ) ){
		        if( s.tl != b.tl ) dif = 0;
		        //s = e;
                s.tl = e.tl;
                s.ad = e.ad;

		        // 置換コマンドを登録
		        b.ad += dif; e.ad += dif;

                mcr.Add(new Replace(b, e, ReplaceWord));

                dif -= e.ad - b.ad - ReplaceWord.Length;

                int f = dif;
	        }

            //if( mcr.size() > 0 ){
            //    // ここで連続置換
            //    edit.getDoc().Execute( mcr );
            //    // カーソル移動
            //    e.ad = b.ad + ulen;
            //    edit.cursor.MoveCur( e, false );
            //    // 閉じる？
            //    //End( IDOK );
            //}
            if (mcr.Count > 0) {
                // ここで連続置換
                foreach (var cmd in mcr) {
                    //view.GetDocument().Execute(cmd);
                    document.Execute(cmd);  
                }
                // カーソル移動
                e.ad = b.ad + ReplaceWord.Length;
                //view.cursor.MoveCur(e, false);
                view.MoveCursor(e);
            }
        }
    }
}
