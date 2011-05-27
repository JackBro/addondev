using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Drawing;
using System.Runtime.InteropServices;
using System.Windows.Forms;

namespace MF {

    class IconCache {
        [DllImport("Shell32.dll", CharSet = CharSet.Unicode)]
        private static extern uint ExtractIconEx(string lpszFile, int nIconIndex,
            IntPtr[] phiconLarge, IntPtr[] phiconSmall, uint nIcons);
        [DllImport("Shell32.dll", CharSet = CharSet.Unicode)]
        private static extern uint ExtractIconEx(string lpszFile, int nIconIndex,
            IntPtr[] phiconLarge, IntPtr phiconSmall, uint nIcons);
        [DllImport("Shell32.dll", CharSet = CharSet.Unicode)]
        private static extern uint ExtractIconEx(string lpszFile, int nIconIndex,
            IntPtr phiconLarge, IntPtr[] phiconSmall, uint nIcons);
        [DllImport("Shell32.dll", CharSet = CharSet.Unicode)]
        private static extern uint ExtractIconEx(string lpszFile, int nIconIndex,
            IntPtr phiconLarge, IntPtr phiconSmall, uint nIcons);

        private ImageList list=new ImageList();
        private static IconCache inst;
        private ImageList cache = new ImageList();
        private Image diricon;
        public static IconCache Inst {
            get {
                if (inst == null) {
                    inst = new IconCache();
                }
                return inst;
            }
        }

        private IconCache() {

            ImageAddIcon(@"c:\", list);

            SHFILEINFO shFileInfo = new SHFILEINFO();
            NativeMethods.SHGetFileInfo("", 0, out shFileInfo,
                (uint)Marshal.SizeOf(shFileInfo), NativeMethods.SHGFI_ICON |
                NativeMethods.SHGFI_SMALLICON | 0x4);
            if (shFileInfo.hIcon != IntPtr.Zero) {
                diricon = Icon.FromHandle(shFileInfo.hIcon).ToBitmap();
                NativeMethods.DestroyIcon(shFileInfo.hIcon);
            }
            cache.ImageSize = new Size(16, 16);
            cache.ColorDepth = ColorDepth.Depth32Bit;
            //list.ColorDepth = ColorDepth.Depth32Bit;
            //ImageAddIcon("shell32.dll", list);
        }

        public Image getImage(string path, string ext, bool isfile) {
            if (!isfile) {
                return diricon;
            }
            //if(ext==".lnk"){
            //    SHFILEINFO shFileInfo = new SHFILEINFO();
            //    NativeMethods.SHGetFileInfo(path, 0, out shFileInfo,
            //        (uint)Marshal.SizeOf(shFileInfo), NativeMethods.SHGFI_ICON |
            //        NativeMethods.SHGFI_SYSICONINDEX | NativeMethods.SHGFI_OVERLAYINDEX);
            //    var iconIndex = (shFileInfo.iIcon & 0xFFFFFF);
            //    if (shFileInfo.hIcon != IntPtr.Zero) {
            //        NativeMethods.DestroyIcon(shFileInfo.hIcon);
            //    }
            //    Image res;
            //    if (ImageAddIcon(@"%SystemRoot%\System32\shell32.dll", iconIndex, out res)) {
            //        return res;
            //    }
            //    return list.Images[iconIndex];
            //}
            //var i = cache.Images.ContainsKey
            if (!cache.Images.ContainsKey(ext)) {
                SHFILEINFO shFileInfo = new SHFILEINFO();
                NativeMethods.SHGetFileInfo(path, 0, out shFileInfo,
                    (uint)Marshal.SizeOf(shFileInfo), NativeMethods.SHGFI_ICON |
                    NativeMethods.SHGFI_SMALLICON | 0x4);
                if (shFileInfo.hIcon != IntPtr.Zero) {
                    cache.Images.Add(ext, Icon.FromHandle(shFileInfo.hIcon).ToBitmap());
                    NativeMethods.DestroyIcon(shFileInfo.hIcon);
                }
            }
            return cache.Images[ext];
        }

       
        //public bool ImageAddIcon(string dllPath, int index, out Image image) {
        //    image = null;
        //    IntPtr[] small = new IntPtr[1];
        //    if (ExtractIconEx(dllPath, index, IntPtr.Zero, small, 1) > 0) {
        //        if (small[0] != IntPtr.Zero) {
        //            var icon = Icon.FromHandle(small[0]);
        //            image = icon.ToBitmap();
        //            icon.Dispose();
        //            return true;
        //        }  
        //    }
        //    return false;
        //}
        /// <summary>
        /// 指定したパスのDLLのアイコン全てをイメージリストに追加する。
        /// </summary>
        /// <param name="dllPath">指定するアイコンを含んだファイルパス</param>
        /// <param name="orderImageList">アイコンを追加するイメージリスト</param>
        /// <returns>成功時 true、失敗時 false</returns>
        public bool ImageAddIcon(string dllPath, ImageList orderImageList)
        {
            // 指定ファイルに格納されたアイコン総数の取得
            uint iconCnt = ExtractIconEx(dllPath, -1, IntPtr.Zero, IntPtr.Zero, 1);
            if (iconCnt == 0) return false;

            IntPtr[] hIcon = new IntPtr[iconCnt];
            try
            {
                // アイコンハンドル＆ハンドル数の取得
                uint getCnt;
                if (orderImageList.ImageSize.Width <= SystemInformation.SmallIconSize.Width &&
                    orderImageList.ImageSize.Height <= SystemInformation.SmallIconSize.Height)
                    getCnt = ExtractIconEx(dllPath, 0, IntPtr.Zero, hIcon, iconCnt);
                else
                    getCnt = ExtractIconEx(dllPath, 0, hIcon, IntPtr.Zero, iconCnt);
                if (getCnt < 1) return false;   // アイコンがなければ終了
                for (int idx = 0; idx < getCnt; ++idx)
                {
                    if (hIcon[idx] != IntPtr.Zero)
                    {
                        using (Icon icon = Icon.FromHandle(hIcon[idx]))
                            orderImageList.Images.Add(icon);
                    }
                }
                return true;
            }
            finally
            {
                foreach (IntPtr ptr in hIcon)
                    if (ptr != IntPtr.Zero) NativeMethods.DestroyIcon(ptr);
            }
        }
    }

    public class IconMethods {
        public static bool getIcon(string file, out Image image) {
            image = null;
            SHFILEINFO shFileInfo = new SHFILEINFO();
            NativeMethods.SHGetFileInfo(file, 0, out shFileInfo,
                (uint)Marshal.SizeOf(shFileInfo), NativeMethods.SHGFI_ICON |
                NativeMethods.SHGFI_SMALLICON | 0x4);
            if (shFileInfo.hIcon != IntPtr.Zero) {

                image = Icon.FromHandle(shFileInfo.hIcon).ToBitmap();
                NativeMethods.DestroyIcon(shFileInfo.hIcon);
                return true;
            }
            return false;
        }
    }
}
