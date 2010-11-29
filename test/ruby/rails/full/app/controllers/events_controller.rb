class EventsController < ApplicationController
	
  def index
  end

  def show
  end
  
  def get_events
    @events = Event.find(:all, :conditions => ["starttime >= '#{Time.at(params['start'].to_i).to_formatted_s(:db)}' and endtime <= '#{Time.at(params['end'].to_i).to_formatted_s(:db)}'"] )
    events = [] 
    @events.each do |event|
      events << {:id => event.id, :title => event.title, :start => "#{event.starttime.iso8601}", :end => "#{event.endtime.iso8601}"}
    end

    render :text => events.to_json
  end
  
  def edit
    @event = Event.find_by_id(params[:id])
  end
  
  def update
  	@event = Event.find_by_id(params[:event][:id])
		
		case params[:event][:commit_button]
  	when "update"
	    #@event = Event.find_by_id(params[:event][:id])
	    #@event.attributes = params[:event]
			@event.time = params[:event_time]
		  @event.title = "time " + sprintf('%.2f', @event.time) #@event.time		  
		  @event.year = @event.starttime.year
		  @event.month = @event.starttime.month
		  @event.day = @event.starttime.day
		  
		  shour = params[:event_startHH].to_i
			smin = params[:event_startMM].to_i
			@event.starttime = DateTime.new(@event.starttime.year, @event.starttime.month, @event.starttime.day, shour, smin)
			ehour = params[:event_endHH].to_i
			emin = params[:event_endMM].to_i
			@event.endtime = DateTime.new(@event.endtime.year, @event.endtime.month, @event.endtime.day, ehour, emin)
		  
	    @event.save

    when "delete"
    #	p "@event"
    #	p @event
    	@event.destroy
    end
   	
   	render :update do |page|
	      page<<"$('#calendar').fullCalendar( 'refetchEvents' )"
	      page<<"$('#desc_dialog').dialog('destroy')" 
	  end
	end
  
  def destroy
    @event = Event.find_by_id(params[:id])
    #if params[:delete_all] == 'true'
    #  @event.event_series.destroy
    #elsif params[:delete_all] == 'future'
    #  @events = @event.event_series.events.find(:all, :conditions => ["starttime > '#{@event.starttime.to_formatted_s(:db)}' "])
    #  @event.event_series.events.delete(@events)
    #else
      @event.destroy
    #end
    
    render :update do |page|
      page<<"$('#calendar').fullCalendar( 'refetchEvents' )"
      page<<"$('#desc_dialog').dialog('destroy')" 
    end
    
  end

  def new
    @event = Event.new(:endtime => 1.hour.from_now)
#		respond_to do |format|
#		  format.js {render :layout=>false}
#		end

#render :update do |page|
#page.replace_html 'create_event', :partial => 'form'
#page << "$('#create_event_dialog').dialog({
#        title: 'New Event',
#        modal: true,
#        width: 500,
#        close: function(event, ui) { $('#create_event_dialog').dialog('destroy') }
#    });
#"
#end
  end
  
  def create

    @event = Event.new(params[:event])
		@event.title = "time " + params[:event_time]
		@event.time = params[:event_time]
		@event.year = @event.starttime.year
		@event.month = @event.starttime.month
		@event.day = @event.starttime.day

		shour = params[:event_startHH].to_i
		smin = params[:event_startMM].to_i
		@event.starttime = DateTime.new(@event.starttime.year, @event.starttime.month, @event.starttime.day, shour, smin)
		ehour = params[:event_endHH].to_i
		emin = params[:event_endMM].to_i
		@event.endtime = DateTime.new(@event.endtime.year, @event.endtime.month, @event.endtime.day, ehour, emin)

  end

	def dayclick
		y = params['year'].to_i
		m = params['month'].to_i+1
		d = params['day'].to_i

p y
p m
p d
    @events = Event.find(:all, :conditions => ["year = ? and month = ? and day = ?", params['year'].to_i, params['month'].to_i+1, params['day'].to_i] )
p "@events.size"
p @events.size
		if(@events.size>0)
			@event = @events[0]
		else
			@event = Event.new(:starttime=>DateTime.new(y, m, d, 8,45), :endtime => DateTime.new(y, m, d, 17, 15))
			#redirect_to '/events/new'
			#redirect_to :controller => 'events',:action => 'new'
			render :action => 'new'
		end
	end
end
