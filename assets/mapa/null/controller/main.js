Ext.define('null.controller.main', {
    extend: 'Ext.app.Controller',
    config: {
	    routes: {
	        'coord/:x,:y': 'getMover'
	    }
    },
    
    getMover: function(x, y) {
    	setTimeout(function() { 
            var panel = Ext.getCmp("mapa");
            panel.getScrollable().getScroller().scrollTo(parseInt(x), parseInt(y), true);
        }, 1000);
    }
});