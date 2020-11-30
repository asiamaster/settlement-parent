package com.dili.settlement.provider;

import com.dili.ss.domain.BaseOutput;
import com.dili.ss.dto.DTOUtils;
import com.dili.ss.metadata.FieldMeta;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.ss.metadata.ValueProvider;
import com.dili.uap.sdk.domain.Department;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.domain.dto.DepartmentDto;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.dili.uap.sdk.session.SessionContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 部门provider
 */
@Component
public class DepartmentProvider implements ValueProvider {

    @Resource
    private DepartmentRpc departmentRpc;
    @Override
    public List<ValuePair<?>> getLookupList(Object val, Map metaMap, FieldMeta fieldMeta) {
        UserTicket userTicket = SessionContext.getSessionContext().getUserTicket();
        if (userTicket == null) {
            return new ArrayList<>(0);
        }
        DepartmentDto query = DTOUtils.newInstance(DepartmentDto.class);
        query.setFirmCode(userTicket.getFirmCode());
        BaseOutput<List<Department>> baseOutput = departmentRpc.listByExample(query);
        if (!baseOutput.isSuccess()) {
            return new ArrayList<>(0);
        }
        List<Department> itemList = baseOutput.getData();
        return itemList.stream().map(temp -> new ValuePairImpl<>(temp.getName(), temp.getId())).collect(Collectors.toList());
    }

    @Override
    public String getDisplayText(Object val, Map metaMap, FieldMeta fieldMeta) {
        return null;
    }
}
