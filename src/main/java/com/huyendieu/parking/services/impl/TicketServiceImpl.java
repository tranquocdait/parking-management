package com.huyendieu.parking.services.impl;

import com.huyendieu.parking.constants.Constant;
import com.huyendieu.parking.entities.TicketEntity;
import com.huyendieu.parking.entities.summary.ParkingAreaSummaryEntity;
import com.huyendieu.parking.exception.ParkingException;
import com.huyendieu.parking.model.request.SearchTicketRequestModel;
import com.huyendieu.parking.model.request.TicketRequestModel;
import com.huyendieu.parking.model.response.TicketItemResponseModel;
import com.huyendieu.parking.model.response.TicketListResponseModel;
import com.huyendieu.parking.model.response.common.base.ComboboxResponseModel;
import com.huyendieu.parking.model.response.common.base.ListComboboxResponseModel;
import com.huyendieu.parking.repositories.TicketRepository;
import com.huyendieu.parking.repositories.complex.TicketComplexRepository;
import com.huyendieu.parking.services.TicketService;
import com.huyendieu.parking.services.base.BaseService;
import com.huyendieu.parking.services.common.ParkingAreaSummaryService;
import com.huyendieu.parking.utils.DateTimeUtils;
import com.huyendieu.parking.utils.MapperUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl extends BaseService implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketComplexRepository ticketComplexRepository;

    @Autowired
    private ParkingAreaSummaryService parkingAreaSummaryService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public String create(String userName, TicketRequestModel requestModel) throws ParkingException {
        TicketEntity ticketEntity = _mappingTicketEntity(userName, requestModel);
        ticketRepository.save(ticketEntity);

        return ticketEntity.getId().toString();
    }

    @Override
    public String update(String userName, TicketRequestModel requestModel) throws ParkingException {

        // deactivate parent ticket
        Optional<TicketEntity> optionalTicketParentEntity = ticketRepository.findById(requestModel.getId());
        if (optionalTicketParentEntity.isEmpty()) {
            String messageError = messageSource.getMessage("data-does-not-exist",
                    new Object[]{requestModel.getId()}, LocaleContextHolder.getLocale());
            throw new ParkingException(messageError);
        }
        TicketEntity ticketParentEntity = optionalTicketParentEntity.get();
        ticketParentEntity.setActive(false);
        ticketParentEntity.setUpdatedDate(currentDate());
        ticketParentEntity.setUpdatedBy(getClass().getSimpleName());
        ticketRepository.save(ticketParentEntity);

        // create a ticket
        TicketEntity ticketEntity = _mappingTicketEntity(userName, requestModel);
        ticketEntity.setParentId(requestModel.getId());
        ticketRepository.save(ticketEntity);

        return ticketEntity.getId().toString();
    }

    @Override
    public String delete(String id) throws ParkingException {
        // deactivate parent ticket
        Optional<TicketEntity> optionalTicketEntity = ticketRepository.findById(id);
        if (optionalTicketEntity.isEmpty()) {
            String messageError = messageSource.getMessage("data-does-not-exist",
                    new Object[]{id}, LocaleContextHolder.getLocale());
            throw new ParkingException(messageError);
        }
        TicketEntity ticketEntity = optionalTicketEntity.get();
        ticketRepository.delete(ticketEntity);

        return ticketEntity.getId().toString();
    }

    @Override
    public ListComboboxResponseModel getTypes() {
        Constant.TicketType[] ticketTypes = Constant.TicketType.values();
        List<ComboboxResponseModel> dataList = new ArrayList<>();
        for (Constant.TicketType ticketType : ticketTypes) {
            ComboboxResponseModel responseModel = ComboboxResponseModel.builder()
                    .key(ticketType.getKey())
                    .value(ticketType.getValue())
                    .code(ticketType.getCode())
                    .build();
            dataList.add(responseModel);
        }
        return ListComboboxResponseModel.builder()
                .dataList(dataList)
                .build();
    }

    @Override
    public TicketListResponseModel getTickets(String userName, SearchTicketRequestModel requestModel) {
        List<TicketItemResponseModel> ticketItemResponseModels = new ArrayList<>();
        List<TicketEntity> ticketEntities = ticketComplexRepository.findAllByPaging(requestModel, userName);
        long totalRecord = ticketComplexRepository.countAll(requestModel, userName);

        if (!CollectionUtils.isEmpty(ticketEntities)) {
            for (TicketEntity ticketEntity : ticketEntities) {
                ticketItemResponseModels.add(mappingTicketData(ticketEntity));
            }
        }
        return TicketListResponseModel.builder()
                .dataList(ticketItemResponseModels)
                .totalRecord(totalRecord)
                .build();
    }

    private TicketItemResponseModel mappingTicketData(TicketEntity ticketEntity) {
        TicketItemResponseModel responseModel = MapperUtils.map(ticketEntity, TicketItemResponseModel.class);
        responseModel.setId(ticketEntity.getId().toString());
        Constant.TicketType ticketType = Constant.TicketType.findByKey(ticketEntity.getType());
        if (ticketType != null) {
            responseModel.setName(ticketType.getValue());
        }
        responseModel.setCreatedDate(DateTimeUtils.convertDateTimeFormat(
                ticketEntity.getCreatedDate(),
                Constant.DateTimeFormat.YYYY_MM_DD_HH_MM_SS,
                Constant.DateTimeFormat.DD_MM_YYYY_HH_MM_SS
        ));

        return responseModel;
    }

    private TicketEntity _mappingTicketEntity(String userName, TicketRequestModel requestModel) throws ParkingException {
        ParkingAreaSummaryEntity parkingArea = parkingAreaSummaryService.mappingSummaryByUsername(userName);
        return TicketEntity.builder()
                .parkingArea(parkingArea)
                .fare(requestModel.getFare())
                .type(requestModel.getType())
                .parentId(requestModel.getId())
                .active(true)
                .createdDate(currentDate())
                .createdBy(getClass().getSimpleName())
                .build();
    }
}
