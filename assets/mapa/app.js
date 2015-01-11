//<debug>
Ext.Loader.setPath({
    'Ext': 'src'
});
//</debug>

/**
 * This simple example shows the ability of the Ext.List component.
 *
 * In this example, it uses a grouped store to show group headers in the list. It also
 * includes an indicator so you can quickly swipe through each of the groups. On top of that
 * it has a disclosure button so you can disclose more information for a list item.
 */
var zom =  .50;
Ext.define('Kitchensink.view.Menus', {
    id: 'mapa',
    extend: 'Ext.Container',
    requires: ['Ext.Menu'],
    config: {
        //padding: 20,
        defaults: {
            xtype : 'button',
            //cls   : 'demobtn',
            margin: '0'
        },
        items: [
            {
                xtype: 'toolbar',
                docked: 'top',
                ui: 'light',
                title: '',
                style: {'background-color': 'rgba(255, 125, 0, 0.0)', 'background-image': 'none', 'position': 'absolute', 'z-index': '1000', 'border-bottom':'0px'},
                items: [
                    {
                        xtype:'spacer'
                    },
                    {
                        iconCls: 'list', 
                        iconMask: true,
                        ui: 'plain',
                        handler: function(){
                            Ext.Viewport.toggleMenu('left');
                        }                  
                    }
                ]
            },
            {
                xtype: 'button',
                text: "-",
                style: { 'position': 'fixed', 'bottom': '33px', 'right': '10px', 'width':'35px', 'z-index': '20000', 'opacity': '0.94' },
                handler:function(){
                    if( zom > 0.55 ){
                        zom -= .05;
                        zom = (parseFloat(zom.toPrecision(2)));
                        document.getElementById('org' ).style.zoom = zom;
                    } else {
                        document.getElementById('org' ).style.zoom = "";
                    }
                }
            },
            {
                xtype: 'button',
                text: "+",
                style: { 'position': 'fixed', 'bottom': '75px', 'right': '10px', 'width':'35px', 'z-index': '20000', 'opacity': '0.94' },
                handler:function(){
                    if( zom < 1.0 ){
                        zom += .05;
                        zom = (parseFloat(zom.toPrecision(2)));
                    }
                    document.getElementById('org' ).style.zoom = zom;
                }
            },
            {
                xtype: 'panel',
                height: '100%',
                scrollable: true,
                items: [
                    {
                        xtype: 'component',
                        scrollable: true,
                        styleHtmlContent: true,
                        html: '<style type="text/css">\n.x-html, .x-html th, .x-html td, .x-html caption { padding:0px!important; }\nhtml, body, * {\nmargin: 0px;\npadding: 0px;\n}\ntd img {display: block; }\ndiv.mascara {\ntop: 0px;\nposition: absolute;pointer-events: none;\nz-index: 2;\n}\ndiv.original {\nzoom:0.5;\nz-index: 1;\n}\n</style>\n'
                              + '<div class="original" id="org">\n'
                              + '    <img name="mapa" src="images/mapa.png" width="1400" height="1050" id="mapa" usemap="#m_mapa" alt="" />'
                              + '    <map name="m_mapa" id="m_mapa">'
                              + '    <area shape="rect" coords="612,749,642,781" href="sponsor:e7aa97edc9af1eb37da3e028ea0c250f" alt="" />'
                              + '    <area shape="rect" coords="861,795,890,824" href="sponsor:e7aa97edc9af1eb37da3e028ea0c32a1" alt="" />'
                              + '    <area shape="rect" coords="809,795,861,824" href="sponsor:e7aa97edc9af1eb37da3e028ea016591" alt="" />'
                              + '    <area shape="rect" coords="1000,626,1033,660" href="sponsor:e7aa97edc9af1eb37da3e028ea0bc30b" alt="" />'
                              + '    <area shape="rect" coords="1000,456,1033,491" href="sponsor:e7aa97edc9af1eb37da3e028ea0c114e" alt="" />'
                              + '    <area shape="rect" coords="1000,491,1033,525" href="sponsor:e7aa97edc9af1eb37da3e028ea0c18dc" alt="" />'
                              + '    <area shape="rect" coords="846,444,910,468" href="sponsor:e7aa97edc9af1eb37da3e028ea0c250f" alt="" />'
                              + '    <area shape="rect" coords="769,444,833,468" href="sponsor:e7aa97edc9af1eb37da3e028ea0c250f" alt="" />'
                              + '    <area shape="rect" coords="687,444,751,468" href="sponsor:e7aa97edc9af1eb37da3e028ea019021" alt="" />'
                              + '    <area shape="rect" coords="573,693,606,727" href="sponsor:e7aa97edc9af1eb37da3e028ea015183" alt="" />'
                              + '    <area shape="rect" coords="573,623,606,693" href="sponsor:e7aa97edc9af1eb37da3e028ea015f12" alt="" />'
                              + '    <area shape="rect" coords="573,589,606,623" href="sponsor:e7aa97edc9af1eb37da3e028ea03f558" alt="" />'
                              + '    <area shape="rect" coords="573,554,606,589" href="sponsor:e7aa97edc9af1eb37da3e028ea019065" alt="" />'
                              + '    <area shape="rect" coords="573,520,606,554" href="sponsor:e7aa97edc9af1eb37da3e028ea017f86" alt="" />'
                              + '    <area shape="rect" coords="573,456,606,520" href="sponsor:e7aa97edc9af1eb37da3e028ea014737" alt="" />'
                              + '    </map>'
                              + '    <div class="mascara"><img style="display:none" id="overlay"/></div>'
                              + '</div>'
                    }
                ]
            }
        ]
    },

    doSetHidden: function(hidden) {
        this.callParent(arguments);

        if (hidden) {
            Ext.Viewport.removeMenu('left');
            Ext.Viewport.removeMenu('right');
            Ext.Viewport.removeMenu('bottom');
            Ext.Viewport.removeMenu('top');
        } else {
            Ext.Viewport.setMenu(this.menuForSide('top'), {
                side: 'top'
            });

            Ext.Viewport.setMenu(this.menuForSide('bottom'), {
                side: 'bottom',
                cover: false
            });

            Ext.Viewport.setMenu(this.menuForSide('left'), {
                side: 'left',
                reveal: true
            });

            Ext.Viewport.setMenu(this.menuForSide('right'), {
                side: 'right',
                reveal: true
            });
        }
    },

    menuForSide: function(side) {

        var items = [
            {
                xtype: 'list',
                style: 'background: url("bg.png");',
                cls: 'myList',
                itemTpl: '{title}',
                listeners: {
                    itemtap: function (list, index, target, record, e, eOpts) {
                        switch( index ){
                            case 0:
                                document.getElementById("overlay" ).style.display = 'block';
                                document.getElementById("overlay").src = "salones_mask.png";
                                Ext.Viewport.toggleMenu('left');
                                break;
                            case 1:
                                document.getElementById("overlay" ).style.display = 'block';
                                document.getElementById("overlay").src = "sponsors_mask.png";
                                Ext.Viewport.toggleMenu('left');
                                break;
                            case 2:
                                document.getElementById("overlay" ).style.display = 'block';
                                document.getElementById("overlay").src = "wc_mask.png";
                                Ext.Viewport.toggleMenu('left');
                                break;
                            case 3:
                                document.getElementById("overlay" ).style.display = 'none';
                                Ext.Viewport.toggleMenu('left');
                                break;
                        }
                    } 
                },

                data: [
                            { icon : "conferencias.png", title: "Salones" },
                            { icon : "stands.png", title: "Patrocinadores" },
                            { icon : "wc.png",   title: "Baños" },
                            { icon : "all.png", title: "Todos" }
                        ]
            }
        ];

        var className = 'Ext.Menu';
        if (Ext.theme.name == "Blackberry") {
            if (['top', 'bottom'].indexOf(side) != -1) {
                className = 'Ext.ux.ApplicationMenu';
            } else {
                className = 'Ext.ux.ContextMenu';
            }
        }

        return Ext.create(className, {
            style: 'margin: 0em;padding:10px;background:url("bg.png")',
            layout: 'fit',
            items: items
        });
    }
});


Ext.application({

    startupImage: {
        '320x460': 'resources/startup/Default.jpg', // Non-retina iPhone, iPod touch, and all Android devices
        '640x920': 'resources/startup/640x920.png', // Retina iPhone and iPod touch
        '640x1096': 'resources/startup/640x1096.png', // iPhone 5 and iPod touch (fifth generation)
        '768x1004': 'resources/startup/768x1004.png', //  Non-retina iPad (first and second generation) in portrait orientation
        '748x1024': 'resources/startup/748x1024.png', //  Non-retina iPad (first and second generation) in landscape orientation
        '1536x2008': 'resources/startup/1536x2008.png', // : Retina iPad (third generation) in portrait orientation
        '1496x2048': 'resources/startup/1496x2048.png' // : Retina iPad (third generation) in landscape orientation
    },

    isIconPrecomposed: false,
    icon: {
        57: 'resources/icons/icon.png',
        72: 'resources/icons/icon@72.png',
        114: 'resources/icons/icon@2x.png',
        144: 'resources/icons/icon@144.png'
    },

    requires: [
        'Ext.MessageBox',
        'Ext.data.Store',
        'Ext.List',
        'Ext.plugin.PullRefresh'
    ],

    controllers: [ "main" ],

    launch: function() {
        var listConfiguration = this.getContainer();
        Ext.Viewport.add(listConfiguration);
    },

    getContainer: function() {
        return Ext.create('Kitchensink.view.Menus');
    }

});
