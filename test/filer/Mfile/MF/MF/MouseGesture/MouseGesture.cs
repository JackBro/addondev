using System;
using System.Drawing;
using System.Windows.Forms;
using System.Runtime.InteropServices;

namespace MouseGesture_Net
{
	/// <summary>
	/// マウスジェスチャの判定を行う
	/// </summary>
	public class MouseGesture
	{
		/// <summary>
		/// 判定距離
		/// </summary>
		public int Range
		{
			
			get
			{
				return range;
			}
			set
			{
				range = value;
			}
		}
		
		/// <summary>
		/// マウスジェスチャの有効無効
		/// </summary>
		private bool enable;
		
		/// <summary>
		/// 判定距離
		/// </summary>
		private int range;
		
		/// <summary>
		/// 四方向それぞれについての情報(要素の大きさは４)
		/// </summary>
		private Direction []direction;

		/// <summary>
		/// マウスをキャプチャしているコントロール
		/// </summary>
		private Control control_;
		
		/// <summary>
		/// 移動距離計算用の古い位置
		/// </summary>
		private Point old;

		public MouseGesture()
		{
			enable =false;
			direction =new Direction[4];
			for(int i =0;i<4;i++)
			{
				direction[i] =new Direction();
			}
			range = 15;
		}

		/// <summary>
		/// マウスジェスチャの開始
		/// </summary>
		/// <param name="control">マウスジェスチャの対象となるコントロール</param>
		/// <param name="pos">現在のマウス位置</param>
		public void Start(Control control,Point pos)
		{
			enable =true;
			resetDirection();
			old =pos;

			control_ =control;
			control_.Capture =true;
		}

		/// <summary>
		/// マウスジェスチャの終わり
		/// </summary>
		public void End()
		{
			if(enable)
			{
				enable =false;
				control_.Capture = false;
			}
		}

		/// <summary>
		/// マウスジェスチャの判定
		/// </summary>
		/// <param name="pos">マウスの位置</param>
		/// <returns>ジェスチャ</returns>
		public Arrow Test(Point pos)
		{	
			//有効なときだけ判定する。
			if(enable)
			{
				int ox = old.X,oy = old.Y;
				Arrow arrow = Arrow.none;
				
				//情報を入れ替えておく
				old =pos;
		
				//移動量を判定して縦横どっちに動くかを判定
				if(Math.Abs(ox - pos.X) > Math.Abs(oy - pos.Y))
				{
					if(ox > pos.X)
					{
						direction[(int)Arrow.left].Length += ox - pos.X;
						direction[(int)Arrow.right].Length = 0;
						arrow = Arrow.left;
					}
					else if(pos.X >ox)
					{
						direction[(int)Arrow.right].Length += pos.X - ox;
						direction[(int)Arrow.left].Length = 0;
						arrow = Arrow.right;
					}
				}
				else
				{
					if(oy > pos.Y)
					{
						direction[(int)Arrow.up].Length += oy - pos.Y;
						direction[(int)Arrow.down].Length = 0;
						arrow = Arrow.up;
					}
					else if(pos.Y >oy)
					{
						direction[(int)Arrow.down].Length += pos.Y - oy;
						direction[(int)Arrow.up].Length = 0;
						arrow = Arrow.down;
					}
				}
	
				//移動を検知したとき
				if(arrow != Arrow.none)
				{
					if(direction[(int)arrow].Enable &&
						direction[(int)arrow].Length > range)
					{
						resetDirection();
			
						//同じ向きが２度入力されないようにする。
						direction[(int)arrow].Enable=false;
					
						return arrow;
					}
				}
			}
			return Arrow.none;
		}

		/// <summary>
		/// ４方向の情報をリセットする
		/// </summary>
		private void resetDirection()
		{
			for(int i=0;i<4;i++)
			{
				direction[i].Reset();
			}
		}

		#region インラインクラス
		/// <summary>
		/// 一定の方向に関する情報を持つ
		/// </summary>
		public class Direction
		{
			/// <summary>
			/// 有効無効
			/// </summary>
			public bool Enable;
			
			/// <summary>
			/// 累計移動距離
			/// </summary>
			public int Length;

			/// <summary>
			/// リセットします。
			/// </summary>
			public void Reset()
			{
				Enable = true;
				Length = 0;
			}

			public Direction()
			{
				Reset();
			}
		}	
		#endregion
	}
}
