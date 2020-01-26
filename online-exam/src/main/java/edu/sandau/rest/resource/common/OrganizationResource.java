package edu.sandau.rest.resource.common;

import edu.sandau.entity.Organization;
import edu.sandau.service.OrganizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Slf4j
@Path("common/org")
@Api(value = "院校组织接口")
public class OrganizationResource {
    @Autowired
    private OrganizationService organizationService;

    @ApiOperation(value = "新增院校")
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response addOrg(Organization organization) throws Exception {
        organizationService.save(organization);
        return Response.ok(organization).build();
    }

    @ApiOperation(value = "更改院校")
    @PUT
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public Response updateOrg(Organization organization) throws Exception {
        organization = organizationService.update(organization);
        return Response.ok(organization).build();
    }

    @ApiOperation(value = "根据上级id查询下级元素")
    @GET
    @Path("{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Organization> getOrgByUpperId(@PathParam("id") Integer id) throws Exception {
        return organizationService.getOrgByUpperId(id);
    }

    @ApiOperation(value = "查询所有学校")
    @GET
    @Path("school")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Organization> getAllSchool() throws Exception {
        return organizationService.getALLSchool();
    }

    @ApiOperation(value = "查询学院")
    @GET
    @Path("collage/{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Organization> getCollage(@PathParam("id") Integer id) throws Exception {
        return organizationService.getCollegesBySchool(id);
    }

    @ApiOperation(value = "查询专业")
    @GET
    @Path("major/{id}")
    @Consumes({ MediaType.APPLICATION_JSON })
    @Produces({ MediaType.APPLICATION_JSON })
    public List<Organization> getMajor(@PathParam("id") Integer id) throws Exception {
        return organizationService.getMajorByCollege(id);
    }

}
