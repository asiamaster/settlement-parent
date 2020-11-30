<script>
    /** 页面初始化函数 */
    $(function() {
        //初始化查询选项
        queryTypeChangeHandler();
        $('input[name="queryType"]').change(queryTypeChangeHandler);
        $('#btn-swipe-card').click(swipeCardClickHandler);
        $('#btn-query').click(queryCustomerHandler);
        $('#btn-clear').click(clearClickHandler);
        $('#keyword').keydown(function(e) {
            if (e.keyCode === 13) {
                queryCustomerHandler();
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
            let row = $(this).closest('tr');
            chooseCustomerCompleteHandler(row);
        });
    });

    /** 查询单选框change事件处理器 */
    function queryTypeChangeHandler() {
        $('#customer-info').addClass("d-none");
        $('#settle-order-list').addClass("d-none");
        let queryType = $('input[name="queryType"]:checked').val();
        switch (queryType) {
            case "1":
                $('#keyword').val("").prop("placeholder", "请输入客户姓名");
                $('#btn-swipe-card').parent().addClass("d-none");
                break;
            case "2":
                $('#keyword').val("").prop("placeholder", "请输入证件号或刷卡");
                $('#btn-swipe-card').parent().removeClass("d-none");
                break;
            case "3":
                $('#keyword').val("").prop("placeholder", "请输入园区卡号或刷卡");
                $('#btn-swipe-card').parent().removeClass("d-none");
                break;
            default:
                $('#keyword').val("").prop("placeholder", "请输入客户姓名");
                $('#btn-swipe-card').parent().addClass("d-none");
                break;
        }
    }

    /** 客户查询处理器 */
    function queryCustomerHandler() {
        $('#customer-info').addClass("d-none");
        $('#settle-order-list').addClass("d-none");
        let keyword = $('#keyword').val();
        let queryType = $('input[name="queryType"]:checked').val();
        if (keyword === undefined || $.trim(keyword) === '') {
            return;
        }
        let params = {};
        params.keyword = $.trim(keyword);
        params.queryType = $.trim(queryType);
        requestCustomerHandler(params);
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
        $('#customer-info').removeClass("d-none");
        $('#customer-info').html(template('template-customer-info', cus));
        loadCustomerOrdersHandler(cus.id);
    }

    /** 清除按钮点击事件处理器 */
    function clearClickHandler() {
        $('#customer-info').addClass("d-none");
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
                    queryCustomerHandler();
                });
                break;
            case "3":
                cardReader(function(cardNo) {
                    $("#keyword").val(cardNo);
                    queryCustomerHandler();
                });
                break;
        }
    }

    /** 结算结果处理器 */
    function settleResultHandler(result) {
        refreshTableHandler();
        let message = '当前共选择 '+result.totalNum+' 笔业务, <span style="color: red;">'+result.successNum+'</span> 笔业务成功, 结算成功总金额 <span style="color: red;">'+result.successAmountView+'</span>, 是否确认打印票据?';
        bs4pop.confirm(message, {backdrop:"static"}, function(sure) {
            if (sure) {
                bui.loading.show("票据打印中,请稍后。。。");
                for (let settleOrder of result.successItemList) {
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
        <div class="col-2">{{name}}</div>
        <div class="col-2">{{certificateNumber}}</div>
        <div class="col-1">{{contactsPhone}}</div>
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
<script>
    $(function(){
        $(document).on('click', '#table-customer-list tr', function () {
            $(this).find('[name="customerRadio"]').prop('checked', true);
        })
    })
</script>