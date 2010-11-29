class CreateEvents < ActiveRecord::Migration
  def self.up
    create_table :events do |t|
      t.string :title
      t.datetime :starttime
      t.datetime :endtime
      t.decimal :time
      t.integer :year
      t.integer :month
      t.integer :day

      t.timestamps
    end
  end

  def self.down
    drop_table :events
  end
end
