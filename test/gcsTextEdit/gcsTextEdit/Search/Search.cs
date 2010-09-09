using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace AsControls
{
    public class Search
    {
        private gcsTextEdit edit;
        public ISearch Searcher { get; set; }

        private string findstr;
        private string replstr;

        public string searchstr;

        public Search(gcsTextEdit edit)
        {
            this.edit = edit;
        }

        public void FindPrevImpl(){
	        // カーソル位置取得
	        VPos stt = new VPos();
            VPos end = new VPos();
	        edit.cursor.getCurPos( out stt, out end );

	        if( stt.ad!=0 || stt.tl!=0 ){
		        // 選択範囲先頭の１文字前から検索
		        DPos s;
		        if( stt.ad == 0 )
			        s = new DPos( stt.tl-1, edit.Document.len(stt.tl-1) );
		        else
			        s = new DPos( stt.tl, stt.ad-1 );

		        // 検索
                DPos b = new DPos(); DPos e = new DPos();
		        if( FindPrevFromImpl( s, ref b, ref e ) ){
			        // 見つかったら選択
			        edit.cursor.MoveCur( b, false );
			        edit.cursor.MoveCur( e, true );
			        return;
		        }
	        }

	        // 見つからなかった場合
	        //NotFound();
        }
        public bool FindPrevFromImpl( DPos s, ref DPos beg, ref DPos end ){
	        // １行ずつサーチ
	        Document d = edit.Document;
	        for( int mbg=0,med=0; ; s.ad=d.len(--s.tl) ){
		        if( Searcher.Search(
			        d.tl(s.tl).ToString(), d.len(s.tl), s.ad, ref mbg, ref med ) ){
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
	        VPos stt, end;
	        edit.cursor.getCurPos( out stt, out end );

	        // 選択範囲ありなら、選択範囲先頭の１文字先から検索
	        // そうでなければカーソル位置から検索
	        DPos s = new DPos(stt.tl, stt.ad);
	        if( stt != end )
                if (stt.ad == edit.Document.len(stt.tl))
			        s = new DPos( stt.tl+1, 0 );
		        else
			        s = new DPos( stt.tl, stt.ad+1 );

	        // 検索
            DPos b = new DPos();
            DPos e = new DPos();
	        if( FindNextFromImpl( s, ref b, ref e ) ){
		        // 見つかったら選択
                edit.cursor.MoveCur(b, false);
                edit.cursor.MoveCur(e, true);
		        return;
	        }

	        // 見つからなかった場合
	        //NotFound();
        }

        public bool FindNextFromImpl( DPos s, ref DPos beg, ref DPos end ){
	        // １行ずつサーチ
	        Document d = edit.Document;
	        for( int mbg=0,med=0,e=d.tln(); s.tl<e; ++s.tl, s.ad=0 )
                if (Searcher.Search(d.tl(s.tl).ToString(), d.len(s.tl), s.ad, ref mbg, ref med)) {
			        beg.tl = end.tl = s.tl;
			        beg.ad = mbg;
			        end.ad = med;
			        return true; // 発見
		        }
	        return false;
        }

        public void ReplaceImpl(){
	        // カーソル位置取得
	        VPos stt = new VPos();
            VPos end = new VPos();
	        edit.cursor.getCurPos( out stt, out end );

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

                    //edit.Document.Replace(b, e, replstr);
                    //edit.Document.Replace(new VPos(b), new VPos(e), replstr);
                    edit.Document.Execute(new Replace(b, e, replstr));
			        //replStr_.FreeWCMem( ustr );

                    if (FindNextFromImpl(new DPos(b.tl, b.ad + replstr.Length), ref b, ref e))
			        {
				        // 次を選択
				        edit.cursor.MoveCur( b, false );
				        edit.cursor.MoveCur( e, true );
				        return;
			        }
		        }
		        else
		        {
			        // そうでなければとりあえず選択
			        edit.cursor.MoveCur( b, false );
			        edit.cursor.MoveCur( e, true );
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

	        // 文書の頭から検索
	        int dif=0;
            DPos s = new DPos(0, 0);//s(0,0)
            DPos b = new DPos();
            DPos e = new DPos();
	        while( FindNextFromImpl( s, ref b, ref e ) ){
		        if( s.tl != b.tl ) dif = 0;
		        s = e;

		        // 置換コマンドを登録
		        b.ad += dif; e.ad += dif;
                
		        //mcr.Add( new doc::Replace(b,e,ustr,ulen) );
                //edit.Document.Replace(new VPos(b), new VPos(e), replstr);
                dif -= e.ad - b.ad - replstr.Length;
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

            //TCHAR str[255];
            //::wsprintf( str, String(IDS_REPLACEALLDONE).c_str(), mcr.size() );
            //MsgBox( str, String(IDS_APPNAME).c_str(), MB_ICONINFORMATION );

            //replStr_.FreeWCMem( ustr );
        }

    }
}
