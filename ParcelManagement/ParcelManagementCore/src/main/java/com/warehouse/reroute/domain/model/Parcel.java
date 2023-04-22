package com.warehouse.reroute.domain.model;


import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.vo.Recipient;
import com.warehouse.reroute.domain.vo.Sender;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Parcel {
     Sender sender;
     Recipient recipient;
     Size parcelSize;
}
