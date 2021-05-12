<script>
    $(function() {
        $('#btn-certain-pay').click(payCertainClickHandler);
    });

    /** 支付页面确定按钮点击事件处理器 */
    function payCertainClickHandler() {
        let mchId = $('[name="mchRadio"]:checked').val();
        mchId = mchId ? mchId : "";
        let customerId = $('#settle-customer-id').val();
        customerId = customerId ? customerId : "";
        let rows = $('#table_' + mchId).find('[name="chk"]:checked');
        if (null == rows || rows.length === 0) {
            showWarning('请至少选中一条数据');
            return;
        }
        let idList = [];
        for (let row of rows) {
            idList.push($(row).val());
        }
        let url = "/settleOrder/forwardPay.html?mchId="+mchId+"&customerId="+customerId+"&ids="+idList.join(",");
        bs4pop.dialog({
            id:'dialog-pay',
            content:url,
            title:'结算收费',
            isIframe:true,
            width: '70%',
            height: 500,
            btns:[
                {label: '取消',className: 'btn-secondary'},
                {label: '确定',className: 'btn-primary',onClick:dialogCertainClickHandler}
            ]
        });
    }

    /** 支付弹框确定按钮点击事件处理器 */
    function dialogCertainClickHandler(e, $iframe) {
        bui.loading.show('数据验证中，请稍候。。。');
        let win = $iframe[0].contentWindow;
        if (!win.validatePayForm()) {
            bui.loading.hide();
            return false;
        }
        bui.loading.hide();
        bui.loading.show('努力提交中，请稍候。。。');
        $.ajax({
            type:"POST",
            url:"/settleOrder/pay.action",
            dataType:"json",
            data:$iframe.contents().find('#form-pay').serialize(),
            success:function(result) {
                bui.loading.hide();
                if (result.code === '200') {
                    settleResultHandler(result.data);
                } else {
                    showError(result.message);
                }
            },
            error:function(){
                bui.loading.hide();
                showError("系统异常,请稍后重试");
            }
        });
    }

    let url = '';
    /** 加载客户单据处理器 */
    function loadCustomerOrdersHandler(cusId) {
        $('#settle-order-list').removeClass("d-none");
        url = "/settleOrder/listPayOrders.action?customerId="+cusId;
        $('#table-settle-order-list').load(url);
    }

    /** 根据挂号查询单据 */
    function loadTrailerNumberOrdersHandler(trailerNumber) {
        $('#settle-order-list').removeClass("d-none");
        url = "/settleOrder/listPayOrdersByTrailerNumber.action?trailerNumber="+trailerNumber;
        $('#table-settle-order-list').load(url);
    }

    /** 根据提交人ID查询单据 */
    function loadSubmitterOrdersHandler(submitterId) {
        $('#settle-order-list').removeClass("d-none");
        url = "/settleOrder/listPayOrdersBySubmitterId.action?submitterId=" + submitterId;
        $('#table-settle-order-list').load(url);
    }

    /** 刷新表格处理器 */
    function refreshTableHandler() {
        if (url === '') {
            return;
        }
        $('#table-settle-order-list').load(url);
    }
</script>
