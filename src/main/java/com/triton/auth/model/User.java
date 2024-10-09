package com.triton.auth.model;

import com.triton.mscommons.model.BaseUser;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@Document("user")
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class User extends BaseUser {

}
