package com.github.afanas10101111.jtb.client;

import com.github.afanas10101111.jtb.client.dto.GroupDiscussionInfo;
import com.github.afanas10101111.jtb.client.dto.GroupInfo;
import com.github.afanas10101111.jtb.client.dto.GroupRequestArgs;
import kong.unirest.GenericType;
import kong.unirest.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GroupClientImpl implements GroupClient {
    public static final String GROUP_COUNT_PATH = "%s/count";
    public static final String GROUP_BY_ID_PATH = "%s/group%d";

    private final String javarushApiGroupPath;

    public GroupClientImpl(@Value("${javarush.url}") String url) {
        this.javarushApiGroupPath = url + "/groups";
    }

    @Override
    public List<GroupInfo> getGroupList(GroupRequestArgs requestArgs) {
        return Unirest.get(javarushApiGroupPath)
                .queryString(requestArgs.populateQueries())
                .asObject(new GenericType<List<GroupInfo>>() {
                })
                .getBody();
    }

    @Override
    public List<GroupDiscussionInfo> getGroupDiscussionList(GroupRequestArgs requestArgs) {
        Map<String, Object> queries = requestArgs.populateQueries();
        queries.put("includeDiscussion", true);
        return Unirest.get(javarushApiGroupPath)
                .queryString(queries)
                .asObject(new GenericType<List<GroupDiscussionInfo>>() {
                })
                .getBody();
    }

    @Override
    public Integer getGroupCount(GroupRequestArgs requestArgs) {
        return Integer.valueOf(
                Unirest.get(String.format(GROUP_COUNT_PATH, javarushApiGroupPath))
                        .queryString(requestArgs.populateQueries())
                        .asString()
                        .getBody()
        );
    }

    @Override
    public GroupDiscussionInfo getGroupById(Integer id) {
        return Unirest.get(String.format(GROUP_BY_ID_PATH, javarushApiGroupPath, id))
                .asObject(GroupDiscussionInfo.class)
                .getBody();
    }
}
