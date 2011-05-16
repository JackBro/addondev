using System;
using System.Drawing;
using System.Windows.Forms;
using System.Runtime.InteropServices;

namespace MouseGesture_Net
{
	/// <summary>
	/// �}�E�X�W�F�X�`���̔�����s��
	/// </summary>
	public class MouseGesture
	{
		/// <summary>
		/// ���苗��
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
		/// �}�E�X�W�F�X�`���̗L������
		/// </summary>
		private bool enable;
		
		/// <summary>
		/// ���苗��
		/// </summary>
		private int range;
		
		/// <summary>
		/// �l�������ꂼ��ɂ��Ă̏��(�v�f�̑傫���͂S)
		/// </summary>
		private Direction []direction;

		/// <summary>
		/// �}�E�X���L���v�`�����Ă���R���g���[��
		/// </summary>
		private Control control_;
		
		/// <summary>
		/// �ړ������v�Z�p�̌Â��ʒu
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
		/// �}�E�X�W�F�X�`���̊J�n
		/// </summary>
		/// <param name="control">�}�E�X�W�F�X�`���̑ΏۂƂȂ�R���g���[��</param>
		/// <param name="pos">���݂̃}�E�X�ʒu</param>
		public void Start(Control control,Point pos)
		{
			enable =true;
			resetDirection();
			old =pos;

			control_ =control;
			control_.Capture =true;
		}

		/// <summary>
		/// �}�E�X�W�F�X�`���̏I���
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
		/// �}�E�X�W�F�X�`���̔���
		/// </summary>
		/// <param name="pos">�}�E�X�̈ʒu</param>
		/// <returns>�W�F�X�`��</returns>
		public Arrow Test(Point pos)
		{	
			//�L���ȂƂ��������肷��B
			if(enable)
			{
				int ox = old.X,oy = old.Y;
				Arrow arrow = Arrow.none;
				
				//�������ւ��Ă���
				old =pos;
		
				//�ړ��ʂ𔻒肵�ďc���ǂ����ɓ������𔻒�
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
	
				//�ړ������m�����Ƃ�
				if(arrow != Arrow.none)
				{
					if(direction[(int)arrow].Enable &&
						direction[(int)arrow].Length > range)
					{
						resetDirection();
			
						//�����������Q�x���͂���Ȃ��悤�ɂ���B
						direction[(int)arrow].Enable=false;
					
						return arrow;
					}
				}
			}
			return Arrow.none;
		}

		/// <summary>
		/// �S�����̏������Z�b�g����
		/// </summary>
		private void resetDirection()
		{
			for(int i=0;i<4;i++)
			{
				direction[i].Reset();
			}
		}

		#region �C�����C���N���X
		/// <summary>
		/// ���̕����Ɋւ����������
		/// </summary>
		public class Direction
		{
			/// <summary>
			/// �L������
			/// </summary>
			public bool Enable;
			
			/// <summary>
			/// �݌v�ړ�����
			/// </summary>
			public int Length;

			/// <summary>
			/// ���Z�b�g���܂��B
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
