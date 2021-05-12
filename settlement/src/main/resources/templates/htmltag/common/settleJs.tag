<script>
    /** 页面初始化函数 */
    $(function() {
        //初始化查询选项
        queryTypeChangeHandler();
        $('input[name="queryType"]').change(queryTypeChangeHandler);
        $('#btn-swipe-card').click(swipeCardClickHandler);
        $('#btn-query').click(queryClickHandler);
        $('#btn-clear').click(clearClickHandler);
        $('#keyword').keydown(function(e) {
            if (e.keyCode === 13) {
                queryClickHandler();
            }
        });
        //选择客户列表 确定按钮事件
        $('#btn-choose-customer').click(function () {
            let arr = $('input[name="customerRadio"]:checked');
            if (arr.length === 0) {
                showInfo("请先选择客户");
                return;
            }
            let row = $(arr[0]).closest('tr');
            chooseCustomerCompleteHandler(row);
        });
        $('#dialog-customer-list').on("dblclick", 'table tr', function() {
            chooseCustomerCompleteHandler($(this));
        });

        //选择提交人列表 确定按钮事件
        $('#btn-choose-submitter').click(function () {
            let arr = $('input[name="submitterRadio"]:checked');
            if (arr.length === 0) {
                showInfo("请先选择提交人");
                return;
            }
            let row = $(arr[0]).closest('tr');
            chooseSubmitterCompleteHandler(row);
        });
        $('#dialog-submitter-list').on("dblclick", 'table tr', function() {
            chooseSubmitterCompleteHandler($(this));
        });
    });

    /** 查询单选框change事件处理器 */
    function queryTypeChangeHandler() {
        $('#middle-block').addClass("d-none");
        $('#settle-order-list').addClass("d-none");
        let queryType = $('input[name="queryType"]:checked').val();
        switch (queryType) {
            case "1":
                $('#keyword').val("").prop("placeholder", "请输入客户名称");
                $('#btn-swipe-card').parent().addClass("d-none");
                break;
            case "2":
                $('#keyword').val("").prop("placeholder", "请输入证件号或刷身份证");
                $('#btn-swipe-card').html("刷身份证");
                $('#btn-swipe-card').parent().removeClass("d-none");
                break;
            case "3":
                $('#keyword').val("").prop("placeholder", "请输入园区卡号或刷卡");
                $('#btn-swipe-card').html("刷卡");
                $('#btn-swipe-card').parent().removeClass("d-none");
                break;
            case "4":
                $('#keyword').val("").prop("placeholder", "请输入挂号");
                $('#btn-swipe-card').parent().addClass("d-none");
                break;
            case "5":
                $('#keyword').val("").prop("placeholder", "请输入姓名/用户名/手机号");
                $('#btn-swipe-card').parent().addClass("d-none");
                break;
            default:
                $('#keyword').val("").prop("placeholder", "请输入客户名称");
                $('#btn-swipe-card').parent().addClass("d-none");
                break;
        }
    }

    /** 查询按钮点击事件处理器 */
    function queryClickHandler() {
        $('#middle-block').addClass("d-none");
        $('#settle-order-list').addClass("d-none");
        let keyword = $.trim($('#keyword').val());
        let queryType = $('input[name="queryType"]:checked').val();
        if (keyword === undefined || keyword === '') {
            return;
        }
        switch (queryType) {
            case "1":
            case "2":
            case "3":
                requestCustomerHandler({keyword:keyword, queryType:queryType});
                break;
            case "4":
                $('#middle-block').removeClass("d-none");
                $('#middle-block').html(template('template-trailer-number-info', {trailerNumber: keyword}));
                loadTrailerNumberOrdersHandler(keyword);
                break;
            case "5":
                requestSubmitterHandler(keyword);
                break;
        }
    }

    /** 请求客户数据处理器 */
    function requestCustomerHandler(params) {
        $.ajax({
            url:"/customer/list.action",
            type:"POST",
            dataType:"json",
            data:params,
            success:function(result) {
                if (result.code === '200') {
                    requestCustomerCompleteHandler(result.data);
                } else {
                    showError(result.message);
                }
            },
            error:function() {
                showError("系统异常,请稍后重试");
            }
        });
    }

    /** 请求客户完成处理器 */
    function requestCustomerCompleteHandler(arr) {
        if (arr.length === 0) {
            showInfo("未查询到客户记录");
            return;
        }
        if (arr.length === 1) {
            certainCustomerHandler(arr[0]);
            return;
        }
        $('#dialog-customer-list .modal-body').html(template('template-customer-list', {customers : arr}));
        $('#dialog-customer-list').modal('show');
    }

    /** 选择客户完成处理器 */
    function chooseCustomerCompleteHandler(row) {
        let cus = {};
        cus.id = row.attr("bind-id");
        cus.name = row.attr("bind-name");
        cus.cellphone = row.attr("bind-cellphone");
        cus.certificateNumber = row.attr("bind-certificate");
        cus.certificateAddr = row.attr("bind-addr");
        cus.contactsPhone = row.attr("bind-contacts-phone");
        $('#dialog-customer-list').modal('hide');
        certainCustomerHandler(cus);
    }

    /** 确定唯一客户处理器 */
    function certainCustomerHandler(cus) {
        $('#middle-block').removeClass("d-none");
        $('#middle-block').html(template('template-customer-info', cus));
        loadCustomerOrdersHandler(cus.id);
    }

    /** 请求提交人处理器 */
    function requestSubmitterHandler(keyword) {
        $.ajax({
            url:"/user/list.action",
            type:"POST",
            dataType:"json",
            data:{
                keyword:keyword
            },
            success:function(result) {
                if (result.code === '200') {
                    requestSubmitterCompleteHandler(result.data);
                } else {
                    showError(result.message);
                }
            },
            error:function() {
                showError("系统异常,请稍后重试");
            }
        });
    }

    /** 请求提交人完成处理器 */
    function requestSubmitterCompleteHandler(arr) {
        if (arr.length === 0) {
            showInfo("未查询到提交人记录");
            return;
        }
        if (arr.length === 1) {
            certainSubmitterHandler(arr[0]);
            return;
        }
        $('#dialog-submitter-list .modal-body').html(template('template-submitter-list', {submitters : arr}));
        $('#dialog-submitter-list').modal('show');
    }

    /** 选择提交人完成处理器 */
    function chooseSubmitterCompleteHandler(row) {
        let submitter = {};
        submitter.id = row.attr("bind-id");
        submitter.realName = row.attr("bind-real-name");
        submitter.userName = row.attr("bind-user-name");
        submitter.cellphone = row.attr("bind-cellphone");
        $('#dialog-submitter-list').modal('hide');
        certainSubmitterHandler(submitter);
    }

    /** 确定唯一提交人处理器 */
    function certainSubmitterHandler(submitter) {
        $('#middle-block').removeClass("d-none");
        $('#middle-block').html(template('template-submitter-info', submitter));
        loadSubmitterOrdersHandler(submitter.id);
    }

    /** 清除按钮点击事件处理器 */
    function clearClickHandler() {
        $('#middle-block').addClass("d-none");
        $('#settle-order-list').addClass("d-none");
        $('#keyword').val("");
    }

    /** 刷卡按钮点击事件处理器 */
    function swipeCardClickHandler() {
        $("#keyword").val("");
        if(typeof(callbackObj) === "undefined"){
            return;
        }
        let queryType = $('input[name="queryType"]:checked').val();
        switch (queryType) {
            case "2":
                idCardReader(function(idCard) {
                    $("#keyword").val(idCard);
                    queryClickHandler();
                });
                break;
            case "3":
                cardReader(function(cardNo) {
                    $("#keyword").val(cardNo);
                    queryClickHandler();
                });
                break;
        }
    }

    /** 结算结果处理器 */
    function settleResultHandler(items) {
        refreshTableHandler();
        let message = '结算成功, 是否确认打印票据？';
        bs4pop.confirm(message, {backdrop:"static"}, function(sure) {
            if (sure) {
                bui.loading.show("票据打印中,请稍后。。。");
                for (let settleOrder of items) {
                    printHandler(settleOrder.id, 1);
                }
                bui.loading.hide();
            }
        });
    }
</script>
<script id="template-customer-info" type="text/html">
    <hr>
    <div class="row">
        <input type="hidden" id="settle-customer-id" value="{{id}}"/>
        <input type="hidden" id="settle-customer-name" value="{{name}}"/>
        <div class="col-2">{{name}}</div>
        <div class="col-2">{{certificateNumber}}</div>
        <div class="col-1">{{contactsPhone}}</div>
    </div>
</script>
<script id="template-submitter-info" type="text/html">
    <hr>
    <div class="row">
        <div class="col-2">{{realName}}</div>
        <div class="col-2">{{userName}}</div>
        <div class="col-1">{{cellphone}}</div>
    </div>
</script>
<script id="template-trailer-number-info" type="text/html">
    <hr>
    <div class="row">
        <div class="col-2">挂号：{{trailerNumber}}</div>
    </div>
</script>
<script id="template-customer-list" type="text/html">
    <table id="table-customer-list" class="table table-bordered table-hover" >
        <thead>
            <tr>
                <th class="text-center align-middle"></th>
                <th class="text-center align-middle">客户名称</th>
                <th class="text-center align-middle">证件号</th>
                <th class="text-center align-middle">联系电话</th>
            </tr>
        </thead>
        <tbody>
            {{each customers cus index}}
                <tr bind-id="{{cus.id}}" bind-name="{{cus.name}}" bind-contacts-phone="{{cus.contactsPhone}}" bind-cellphone="{{cus.cellphone}}" bind-certificate="{{cus.certificateNumber}}" bind-addr="{{cus.certificateAddr}}">
                    <td class="text-center align-middle"><input type="radio" name="customerRadio" value="{{cus.id}}"/></td>
                    <td class="text-center align-middle">{{cus.name}}</td>
                    <td class="text-center align-middle">{{cus.certificateNumber}}</td>
                    <td class="text-center align-middle">{{cus.contactsPhone}}</td>
                </tr>
            {{/each}}
        </tbody>
    </table>
</script>

<script id="template-submitter-list" type="text/html">
    <table id="table-submitter-list" class="table table-bordered table-hover" >
        <thead>
        <tr>
            <th class="text-center align-middle"></th>
            <th class="text-center align-middle">姓名</th>
            <th class="text-center align-middle">用户名</th>
            <th class="text-center align-middle">手机号</th>
        </tr>
        </thead>
        <tbody>
        {{each submitters sub index}}
        <tr bind-id="{{sub.id}}" bind-real-name="{{sub.realName}}" bind-user-name="{{sub.userName}}" bind-cellphone="{{sub.cellphone}}">
            <td class="text-center align-middle"><input type="radio" name="submitterRadio" value="{{sub.id}}"/></td>
            <td class="text-center align-middle">{{sub.realName}}</td>
            <td class="text-center align-middle">{{sub.userName}}</td>
            <td class="text-center align-middle">{{sub.cellphone}}</td>
        </tr>
        {{/each}}
        </tbody>
    </table>
</script>
<script>
    $(function(){
        $(document).on('click', '#table-customer-list tr', function () {
            $(this).find('[name="customerRadio"]').prop('checked', true);
        });

        // 商户单选框事件处理
        $('#table-settle-order-list').on('click', '[name="mchRadio"]', function() {
            let mchId = $(this).val();
            $('#table_' + mchId).find(':checkbox').prop("disabled", false);
            $('#table-settle-order-list table').not('#table_' + mchId).find(':checkbox').prop("checked", false).prop("disabled", true);
        });

        // 表格列头复选框事件
        $('#table-settle-order-list').on('click', '[name="checkAll"]', function() {
            let checked = $(this).prop("checked");
            $(this).closest('table').find(':checkbox').prop("checked", checked);
        });

        // 表格内复选框click事件
        $('#table-settle-order-list').on('click', '[name="chk"]', function() {
            $(this).trigger('change');
        });

        // 表格内复选框change事件
        $('#table-settle-order-list').on('change', '[name="chk"]', function() {
            let checked = true;
            $(this).closest('table').find('[name="chk"]').each(function(){
                if (!$(this).prop("checked")) {
                    checked = false;
                }
            });
            $(this).closest('table').find('[name="checkAll"]').prop("checked", checked);
        });

        // tr单击事件处理
        $('#table-settle-order-list').on('click', 'table tr', function(e) {
            if (e.target.type === 'checkbox') {
                return;
            }
            if ($(e.target).prop('tagName').toLowerCase() === 'a') {
                return;
            }
            let mchId = $(this).closest('table').attr("bind-mchId");
            if (!$('#mchRadio_' + mchId).prop("checked")) {
                return;
            }
            let checked = $(this).find('[name="chk"]').prop('checked');
            $(this).find('[name="chk"]').prop('checked', !checked).trigger('change');
        });
    })
</script>