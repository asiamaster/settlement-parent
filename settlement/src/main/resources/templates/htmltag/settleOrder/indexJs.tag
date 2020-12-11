<script>
    $(function() {
        $('.chk-type-all').on("change", function() {
            $('.chk-type-item').prop("checked", this.checked);
        });

        $('.chk-type-item').on("change", function() {
            if ($('.chk-type-item:checked').length === $('.chk-type-item').length){
                $('.chk-type-all').prop("checked", true);
            } else {
                $('.chk-type-all').prop("checked", false);
            }
        });
        //时间范围
        lay('.laydatetime').each(function () {
            laydate.render({
                theme: '#007bff',
                elem: this
                , trigger: 'click'
                , type: 'datetime'
            });
        });

        //日期范围
        lay('.laydate').each(function () {
            laydate.render({
                theme: '#007bff',
                elem: this
                , trigger: 'click'
                , type: 'date'
            });
        });

        $('#btn-reprint').click(reprintClickHandler);

        $(window).resize(function () {
            $('#grid').bootstrapTable('resetView')
        });
        //初始化表格
        $('#grid').bootstrapTable('refreshOptions', {url: '/settleOrder/listPage.action'});
    });

    /**
     * 查询处理
     */
    function queryDataHandler() {
        $('#grid').bootstrapTable('refresh');
    }

    /**
     * table参数组装
     * 可修改queryParams向服务器发送其余的参数
     * @param params
     */
    function queryParams(params) {
        let temp = {
            rows: params.limit,   //页面大小
            page: ((params.offset / params.limit) + 1) || 1, //页码
            sort: params.sort,
            order: params.order
        }
        return $.extend(temp, bui.util.bindGridMeta2Form('grid', 'queryForm'));
    }

    /** 补打按钮点击事件处理器 */
    function reprintClickHandler() {
        let rows = $('#grid').bootstrapTable('getSelections');
        if (null == rows || rows.length === 0) {
            showWarning('请至少选中一条数据');
            return;
        }
        let row = rows[0];
        if ( !(row.state === ${@com.dili.settlement.enums.SettleStateEnum.DEAL.getCode()} && row.reverse === ${@com.dili.settlement.enums.ReverseEnum.NO.getCode()})) {
            showWarning("非冲正单且状态为已处理记录才能补打票据");
            return;
        }
        let message = '是否确认补打 '+row.businessCode+' 票据?';
        bs4pop.confirm(message, {}, function(sure) {
            if (sure) {
                bui.loading.show("票据打印中,请稍后。。。");
                printHandler(row.id, 2);
                bui.loading.hide();
            }
        });
    }

    /** 结算方式格式化器*/
    function settleWayFormatter(value, row, index) {
        if (row.way !== ${@com.dili.settlement.enums.SettleWayEnum.MIXED_PAY.getCode()}) {
            return value;
        }
        return '<a href="javascript:;" onclick="showSettleWayDetailHandler('+row.id+','+row.reverse+'); return false;">'+ value +'</a>';
    }

    /** 查看组合结算详情*/
    function showSettleWayDetailHandler(id, reverse) {
        $.ajax({
            url:"/settleWayDetail/listBySettleOrderId.action?settleOrderId=" + id + "&reverse=" + reverse,
            type:"POST",
            dataType:"json",
            success:function(result) {
                if (result.code === '200') {
                    $('#dialog-way-detail .modal-body').html(template('template-way-detail', {detailList : result.data}));
                    $('#dialog-way-detail').modal('show');
                } else {
                    showError(result.message);
                }
            },
            error:function() {
                showError("系统异常,请稍后重试");
            }
        });
    }

    /** 行样式处理器 */
    function rowStyleHandler(row, index) {
        if (row.reverse === ${@com.dili.settlement.enums.ReverseEnum.YES.getCode()}) {
            return {
                css:{
                    background: '#FFE0E0'
                }
            }
        }
        return {};
    }
</script>

<script id="template-way-detail" type="text/html">
    <table id="table-way-list" class="table table-bordered table-hover" >
        <thead>
        <tr>
            <th class="text-center align-middle">结算方式</th>
            <th class="text-center align-middle">金额</th>
            <th class="text-center align-middle">流水号</th>
            <th class="text-center align-middle">收款日期</th>
            <th class="text-center align-middle">备注</th>
        </tr>
        </thead>
        <tbody>
        {{each detailList detail index}}
        <tr>
            <td class="text-center align-middle">{{detail.wayText}}</td>
            <td class="text-center align-middle">{{detail.amountText}}</td>
            <td class="text-center align-middle">{{detail.serialNumber ? detail.serialNumber : '-'}}</td>
            <td class="text-center align-middle">{{detail.chargeDate ? detail.chargeDate : '-'}}</td>
            <td class="text-center align-middle">{{detail.notes ? detail.notes : '-'}}</td>
        </tr>
        {{/each}}
        </tbody>
    </table>
</script>