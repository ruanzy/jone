(function ($) {
    var tpl = [];
    tpl.push('<div class="rz-datepicker"><div></div>');
    tpl.push('<i></i>');
    tpl.push('<div style="position:absolute;top:0;right:0;bottom:0;left:0;"></div>');
    tpl.push('<div class="rz-datepicker-popup">');
    tpl.push('<div class="rz-datepicker-top">');
    tpl.push('<div class="rz-year-box"><a class="rz-year-left"><cite></cite></a><input class="rz-year" readonly value=""><a class="rz-year-right"><cite></cite></a>');
    tpl.push('<table class="rz-year-panel">');
    tpl.push('<thead><tr><th colspan=2 class="rz-year-up"><cite></cite></th></thead>');
    tpl.push('<tbody></tbody>');
    tpl.push('<tfoot><tr><th colspan=2 class="rz-year-down"><cite></cite></th></tfoot>');
    tpl.push('</table>');
    tpl.push('</div>');
    tpl.push('<div class="rz-month-box"><a class="rz-month-left"><cite></cite></a><input class="rz-month" readonly value=""><a class="rz-month-right"><cite></cite></a>');
    tpl.push('<table class="rz-month-panel">');
    tpl.push('<tr><td>1月</td><td>2月</td></tr>');
    tpl.push('<tr><td>3月</td><td>4月</td></tr>');
    tpl.push('<tr><td>5月</td><td>6月</td></tr>');
    tpl.push('<tr><td>7月</td><td>8月</td></tr>');
    tpl.push('<tr><td>9月</td><td>10月</td></tr>');
    tpl.push('<tr><td>11月</td><td>12月</td></tr>');
    tpl.push('</table>');
    tpl.push('</div>');
    tpl.push('</div>');
    tpl.push('<table class="rz-calendar"><thead><tr><th>日</th><th>一</th><th>二</th><th>三</th><th>四</th><th>五</th><th>六</th></tr></thead><tbody></tbody></table>');
    tpl.push('<div class="rz-datepicker-bottom">');
    tpl.push('<div class="rz-time">');
    tpl.push('<input type="text" class="rz-hours"  maxlength=2 readonly>:');
    tpl.push('<input type="text" class="rz-minutes"  maxlength=2 readonly>:');
    tpl.push('<input type="text" class="rz-seconds"  maxlength=2 readonly>');
    tpl.push('</div>');
    tpl.push('<div class="rz-buttons" style="border-right: 1px solid #B1D2EC;">');
    tpl.push('<a id="rz_clear" style="display: inline-block;">清空</a>');
    tpl.push('<a id="rz_today" style="display: inline-block;">今天</a>');
    tpl.push('<a id="rz_ok" style="display: inline-block;">确认</a>');
    tpl.push('</div>');
    tpl.push('<table class="rz-hours-panel">');
    tpl.push('<thead><tr><th colspan=4>时</th><th class="rz-hours-close">×</th></tr></thead>');
    tpl.push('<tbody>');
    tpl.push('<tr><td>00</td><td>01</td><td>02</td><td>03</td><td>04</td></tr>');
    tpl.push('<tr><td>05</td><td>06</td><td>07</td><td>08</td><td>09</td></tr>');
    tpl.push('<tr><td>10</td><td>11</td><td>12</td><td>13</td><td>14</td></tr>');
    tpl.push('<tr><td>15</td><td>16</td><td>17</td><td>18</td><td>19</td></tr>');
    tpl.push('<tr><td>20</td><td>21</td><td>22</td><td>23</td><td></td></tr>');
    tpl.push('</tbody>');
    tpl.push('</table>');
    tpl.push('<table class="rz-minutes-panel">');
    tpl.push('<thead><tr><th colspan=9>分钟</th><th class="rz-minutes-close">×</th></tr></thead>');
    tpl.push('<tbody>');
    tpl.push('<tr><td>00</td><td>01</td><td>02</td><td>03</td><td>04</td><td>05</td><td>06</td><td>07</td><td>08</td><td>09</td></tr>');
    tpl.push('<tr><td>10</td><td>11</td><td>12</td><td>13</td><td>14</td><td>15</td><td>16</td><td>17</td><td>18</td><td>19</td></tr>');
    tpl.push('<tr><td>20</td><td>21</td><td>22</td><td>23</td><td>24</td><td>25</td><td>26</td><td>27</td><td>28</td><td>29</td></tr>');
    tpl.push('<tr><td>30</td><td>31</td><td>32</td><td>33</td><td>34</td><td>35</td><td>36</td><td>37</td><td>38</td><td>39</td></tr>');
    tpl.push('<tr><td>40</td><td>41</td><td>42</td><td>43</td><td>44</td><td>45</td><td>46</td><td>47</td><td>48</td><td>49</td></tr>');
    tpl.push('<tr><td>50</td><td>51</td><td>52</td><td>53</td><td>54</td><td>55</td><td>56</td><td>57</td><td>58</td><td>59</td></tr>');
    tpl.push('</tbody>');
    tpl.push('</table>');
    tpl.push('<table class="rz-seconds-panel">');
    tpl.push('<thead><tr><th colspan=9>秒</th><th class="rz-seconds-close">×</th></tr></thead>');
    tpl.push('<tbody>');
    tpl.push('<tr><td>00</td><td>01</td><td>02</td><td>03</td><td>04</td><td>05</td><td>06</td><td>07</td><td>08</td><td>09</td></tr>');
    tpl.push('<tr><td>10</td><td>11</td><td>12</td><td>13</td><td>14</td><td>15</td><td>16</td><td>17</td><td>18</td><td>19</td></tr>');
    tpl.push('<tr><td>20</td><td>21</td><td>22</td><td>23</td><td>24</td><td>25</td><td>26</td><td>27</td><td>28</td><td>29</td></tr>');
    tpl.push('<tr><td>30</td><td>31</td><td>32</td><td>33</td><td>34</td><td>35</td><td>36</td><td>37</td><td>38</td><td>39</td></tr>');
    tpl.push('<tr><td>40</td><td>41</td><td>42</td><td>43</td><td>44</td><td>45</td><td>46</td><td>47</td><td>48</td><td>49</td></tr>');
    tpl.push('<tr><td>50</td><td>51</td><td>52</td><td>53</td><td>54</td><td>55</td><td>56</td><td>57</td><td>58</td><td>59</td></tr>');
    tpl.push('</tbody>');
    tpl.push('</table>');
    tpl.push('</div>');
    tpl.push('</div>');
    tpl.push('</div>');
    tpl = tpl.join('');

    function update() {
        this.find('.rz-year').val(y + '年');
        this.find('.rz-month').val(m + 1 + '月');
        this.find('.rz-hours').val(HH < 10 ? ('0' + HH) : HH);
        this.find('.rz-minutes').val(mm < 10 ? ('0' + mm) : mm);
        this.find('.rz-seconds').val(ss < 10 ? ('0' + ss) : ss);
        var tbody = this.find('.rz-calendar tbody');
        var tds = this.find('.rz-calendar td');
        var c = getcalendar(y, m + 1);
        tbody.html(c);
    }

    function hideAll() {
        this.find('.rz-year-panel').hide();
        this.find('.rz-month-panel').hide();
        this.find('.rz-hours-panel').hide();
        this.find('.rz-minutes-panel').hide();
        this.find('.rz-seconds-panel').hide();
    }

    function buildUpYearPanel(v) {
        var arr = [];
        var begin = v - 11;
        for (var i = 0; i <= 5; i++) {
            arr.push('<tr><td>', begin + 2 * i - 1, '年</td><td>', begin + 2 * i, '年</td></tr>');
        }
        this.find('.rz-year-panel tbody').html(arr.join(''));
    }

    function buildDownYearPanel(v) {
        var arr = [];
        var begin = v + 1;
        for (var i = 0; i <= 5; i++) {
            arr.push('<tr><td>', begin + 2 * i, '年</td><td>', begin + 2 * i + 1, '年</td></tr>');
        }
        this.find('.rz-year-panel tbody').html(arr.join(''));
    }

    function isLeapyear(y) {
        return (y % 4 == 0) && (y % 100 != 0 || y % 400 == 0);
    }

    function daysofmonth(y) {
        return [31, isLeapyear(y) ? 29 : 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    }

    function getcalendar(y, m) {
        var lasty = y;
        var lastm = m;
        if (m == 1) {
            lastm = 11;
            lasty = y - 1;
        } else {
            lastm = m - 2;
        }
        var arr = [];
        var lastdays = daysofmonth(lasty)[lastm];
        var days = daysofmonth(y)[m - 1];
        var week = new Date(y, m - 1, 1).getDay();
        var week2 = new Date(y, m - 1, days).getDay();
        arr.push('<tr>');
        for (i = week; i > 0; i--) {
            arr.push('<td>', lastdays - i + 1, '</td>');
        }
        for (i = 1; i <= 7 - week; i++) {
            arr.push('<td>', i, '</td>');
        }
        arr.push('</tr>');
        for (i = 0; i <= days - (7 - week + 1); i++) {
            var v = 7 - week + 1 + i;
            if (i % 7 == 0) {
                arr.push('<tr>');
            }
            arr.push('<td>', v, '</td>');
            if (i > 0 && (i + 1) % 7 == 0) {
                arr.push('</tr>');
            }
        }
        for (i = 0; i < (6 - week2); i++) {
            arr.push('<td>', i + 1, '</td>');
        }
        return arr.join('');
    }
    $.fn.rdate = function (options) {
        if (typeof options == "string") {
            return arguments.callee.methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        }
        return this.each(function () {
            var me = $(this);
            var opts = $.extend({}, $.fn.rdate.defaults, options);
            me.data('options', opts);
            //var y, m, d, HH, mm, ss;
            me.wrap(tpl);
            var datepicker = me.parents('.rz-datepicker');
            datepicker.click(function (e) {
                $('.rz-datepicker-popup').hide();
                hideAll.call(datepicker);
                var v = me.val();
                var date = new Date();
                if (v == '') {
                    date = new Date();
                } else {
                    date = new Date(Date.parse(v));
                }
                y = date.getFullYear();
                m = date.getMonth();
                d = date.getDate();
                HH = date.getHours();
                mm = date.getMinutes();
                ss = date.getSeconds();
                update.call(datepicker);
                datepicker.find('.rz-datepicker-popup').show();
                e.stopPropagation();
            });
            $(document).on('click', function () {
                datepicker.find('.rz-datepicker-popup').hide();
            });
            datepicker.find('.rz-year-box').click(function (e) {
                hideAll.call(datepicker);
                buildDownYearPanel.call(datepicker, y - 6);
                datepicker.find('.rz-year-panel').show();
                e.stopPropagation();
            });
            datepicker.find('.rz-year-up').click(function (e) {
                e.stopPropagation();
                var vt = datepicker.find('.rz-year-panel tbody td:first').text();
                buildUpYearPanel.call(datepicker, parseInt(vt));
            });
            datepicker.find('.rz-year-down').click(function (e) {
                e.stopPropagation();
                var vt = datepicker.find('.rz-year-panel tbody td:last').text();
                buildDownYearPanel.call(datepicker, parseInt(vt));
            });
            datepicker.find('.rz-month-box').click(function (e) {
                hideAll.call(datepicker);
                datepicker.find('.rz-month-panel').show();
                e.stopPropagation();
            });
            datepicker.find('.rz-year-panel').on('click', 'td', function (e) {
                e.stopPropagation();
                y = parseInt($(this).text());
                datepicker.find('.rz-year-panel').hide();
                update.call(datepicker);
            });
            datepicker.find('.rz-month-panel').on('click', 'td', function (e) {
                e.stopPropagation();
                m = parseInt($(this).text()) - 1;
                datepicker.find('.rz-month-panel').hide();
                update.call(datepicker);
            });
            datepicker.find('.rz-calendar').on('click', 'td', function (e) {
                e.stopPropagation();
                hideAll.call(datepicker);
                datepicker.find('.rz-calendar td.rz-calendar-selected').removeClass('rz-calendar-selected');
                $(this).addClass('rz-calendar-selected');
                d = $(this).text();
            });
            datepicker.find('.rz-hours-panel').on('click', 'td', function (e) {
                e.stopPropagation();
                HH = parseInt($(this).text());
                datepicker.find('.rz-hours-panel').hide();
                update.call(datepicker);
            });
            datepicker.find('.rz-minutes-panel').on('click', 'td', function (e) {
                e.stopPropagation();
                mm = parseInt($(this).text());
                datepicker.find('.rz-minutes-panel').hide();
                update.call(datepicker);
            });
            datepicker.find('.rz-seconds-panel').on('click', 'td', function (e) {
                e.stopPropagation();
                ss = parseInt($(this).text());
                datepicker.find('.rz-seconds-panel').hide();
                update.call(datepicker);
            });
            datepicker.find('#rz_clear').click(function (e) {
                me.val('');
                e.stopPropagation();
            });
            datepicker.find('#rz_today').click(function (e) {
                var y = new Date().getFullYear();
                var m = new Date().getMonth();
                var d = new Date().getDate();
                var HH = new Date().getHours();
                var mm = new Date().getMinutes();
                var ss = new Date().getSeconds();
                HH = HH < 10 ? ('0' + HH) : HH;
                mm = mm < 10 ? ('0' + mm) : mm;
                ss = ss < 10 ? ('0' + ss) : ss;
                var v = [y, m + 1, d].join('-') + ' ' + [HH, mm, ss].join(':');
                me.val(v);
                e.stopPropagation();
                datepicker.find('.rz-datepicker-popup').hide();
            });
            datepicker.find('#rz_ok').click(function (e) {
                var HH = datepicker.find('.rz-hours').val();
                var mm = datepicker.find('.rz-minutes').val();
                var ss = datepicker.find('.rz-seconds').val();
                var v = [y, m + 1, d].join('-') + ' ' + [HH, mm, ss].join(':');
                me.val(v);
                e.stopPropagation();
                datepicker.find('.rz-datepicker-popup').hide();
            });
            datepicker.find('.rz-hours').click(function (e) {
                e.stopPropagation();
                hideAll.call(datepicker);
                datepicker.find('.rz-hours-panel').show();
            });
            datepicker.find('.rz-minutes').click(function (e) {
                e.stopPropagation();
                hideAll.call(datepicker);
                datepicker.find('.rz-minutes-panel').show();
            });
            datepicker.find('.rz-seconds').click(function (e) {
                e.stopPropagation();
                hideAll.call(datepicker);
                datepicker.find('.rz-seconds-panel').show();
            });
            datepicker.find('.rz-hours-close').click(function (e) {
                e.stopPropagation();
                datepicker.find('.rz-hours-panel').hide();
            });
            datepicker.find('.rz-minutes-close').click(function (e) {
                e.stopPropagation();
                datepicker.find('.rz-minutes-panel').hide();
            });
            datepicker.find('.rz-seconds-close').click(function (e) {
                e.stopPropagation();
                datepicker.find('.rz-seconds-panel').hide();
            });
            datepicker.find('.rz-year-left').click(function (e) {
                e.stopPropagation();
                hideAll.call(datepicker);
                y -= 1;
                update.call(datepicker);
            });
            datepicker.find('.rz-year-right').click(function (e) {
                e.stopPropagation();
                hideAll.call(datepicker);
                y += 1;
                update.call(datepicker);
            });
            datepicker.find('.rz-month-left').click(function (e) {
                e.stopPropagation();
                hideAll.call(datepicker);
                if (m == 0) {
                    y -= 1;
                    m = 11;
                } else {
                    m -= 1;
                }
                update.call(datepicker);
            });
            datepicker.find('.rz-month-right').click(function (e) {
                e.stopPropagation();
                hideAll.call(datepicker);
                if (m == 11) {
                    y += 1;
                    m = 0;
                } else {
                    m += 1;
                }
                update.call(datepicker);
            });
        });
    };
    $.fn.rdate.defaults = {
        format: 'yyyy-MM-dd hh:mm:ss',
        onpicked: function () {

        }
    };
    $.fn.rdate.methods = {
        year: function (jq, v) {
                var y = this.find('.y_choose');
                if (v) {
                    y.val(v);
                }
                var v = y.val();
                return parseInt(v);
            },
            month: function (v) {
                var m = this.find('.m_choose');
                if (v) {
                    m.val(v);
                }
                var v = y.val();
                return parseInt(v);
            },
            hour: function (v) {
                var h = this.find('.d_h');
                if (v) {
                    h.val(v);
                }
                var v = h.val();
                return parseInt(v);
            },
            minute: function (v) {
                var m = this.find('.d_m');
                if (v) {
                    m.val(v);
                }
                var v = m.val();
                return parseInt(v);
            },
            second: function (v) {
                var s = this.find('.d_s');
                if (v) {
                    s.val(v);
                }
                var v = s.val();
                return parseInt(v);
            }
    };

    function format(date, format) {
        if (!(date instanceof Date)) {
            return;
        }
        var o = {
            "M+": date.getMonth() + 1,
            "d+": date.getDate(),
            "h+": date.getHours(),
            "m+": date.getMinutes(),
            "s+": date.getSeconds(),
            "q+": Math.floor((date.getMonth() + 3) / 3),
            "S": date.getMilliseconds()
        };
        if (/(y+)/.test(format)) {
            format = format.replace(RegExp.$1, (date.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(format)) {
                format = format.replace(RegExp.$1, RegExp.$1.length == 1 ? o[k] : ("00" + o[k]).substr(("" + o[k]).length));
            }
        }
        return format;
    }
})(jQuery);