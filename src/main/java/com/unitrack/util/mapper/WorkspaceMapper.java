package com.unitrack.util.mapper;

import com.unitrack.dto.WorkspaceDto;
import com.unitrack.entity.Workspace;
import org.mapstruct.Mapper;

@Mapper
public interface WorkspaceMapper {

    WorkspaceDto workspaceToDto(Workspace workspace);
}
