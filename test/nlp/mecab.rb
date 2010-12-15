# -*- coding: windows-31j -*-
require "dl/import"
require "dl/struct"

module MecabFunc
  extend DL::Importer
  dlload 'C:/lib/MeCab/bin/libmecab.dll'
  typealias('size_t', 'unsigned long')
  extern "mecab_t* mecab_new2(const char*)"
  extern "const char* mecab_version()"
  extern "const char* mecab_sparse_tostr(mecab_t*, const char*)"
  extern "const char* mecab_strerror(mecab_t*)"
  extern "void mecab_destroy(mecab_t *)"
end

module MecabLib
  class Mecab
    include MecabFunc
    @mecab=nil
    def initialize(args)
      @mecab=MecabFunc.mecab_new2(args)
    end

    def version()
      MecabFunc.mecab_version()
    end

    def strerror()
      MecabFunc.mecab_strerror(@mecab)
    end

    def sparse_tostr(str)
      MecabFunc.mecab_sparse_tostr(@mecab,str)
    end

    def sparse_tonode(str)
      #http://nlp.sfc.keio.ac.jp/~aihara/nlp.html でのaihara氏の実装です。
      prev=nil
      head=Node.new()
      tmp_str= "#{sparse_tostr(str)}"
      tmp_str.split("\n").each{|line|
        buf=Node.new(line,prev)
        if prev!=nil
          prev.next=buf
        end
        prev=buf

        if head.next==nil
          head.next=buf
        end
      }
      head
    end

    def destroy()
      MecabFunc.mecab_destroy(@mecab)
    end

    class Node
      @prev=nil
      @next=nil

      @surface=nil#形態素の表記
      @pos=nil#品詞
      @root=nil#原形
      @reading=nil#読み
      @pronunciation=nil#発音
      attr_accessor :prev,:next,:surface,:pos,:root,:reading,:pronunciation

      def initialize(line=nil,prev=nil)
        @prev=prev

        if line != nil
          if line == "EOS"#EOSの時
            @surface=line
            @pos="EOS"
            @root="EOS"
            @reading="EOS"
            @pronunciation="EOS"
          else#それ以外
            linels=line.split("\t")
            @surface=linels[0]
            fetls=linels[1].split(",")
            @pos=fetls[0..5].join(",")
            if fetls[6]==nil
              @root=""
            else
              @root=fetls[6]
            end
            if fetls[7]==nil
              @reading=""
            else
              @reading=fetls[7]
            end
            if fetls[8]==nil
              @pronunciation=""
            else
              @pronunciation=fetls[8]
            end
          end
        end
      end

      def hasNext()
        if @next==nil
          false
        else
          true
        end
      end
    end
  end
end
