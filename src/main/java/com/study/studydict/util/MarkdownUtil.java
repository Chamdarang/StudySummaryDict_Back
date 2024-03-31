package com.study.studydict.util;

import com.study.studydict.model.Info;
import com.study.studydict.repository.InfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MarkdownUtil {
    private final InfoRepository infoRepository;
    @Autowired
    public MarkdownUtil(InfoRepository infoRepository){
        this.infoRepository=infoRepository;
    }
    public String makeMarkDown(String contents){
        StringBuilder content= new StringBuilder(contents);
        makeMDofIndent(content);
        makeMDofBold(content);
        makeMDofInfo(content);
        return content.toString();
    }
    private StringBuilder makeMDofInfo(StringBuilder content){
        String markdownTemplate="<div className=\"m-2 rounded-0 border\" key=%d><div className=\"fs-4 fw-bold mb-1\">%s</div><div className=\"text-pre\">%s</div></div>";
        Matcher matcher= Pattern.compile("[\\n]?\\[\\[([\\w가-힣]+[\\w가-힣 ]*)\\]\\][\\n]?").matcher(content);
        while (matcher.find()){
            int[] targetIdx= {matcher.start(),matcher.end()};
            String itemName=matcher.group(1);
            Info info= infoRepository.findByNameIgnoreCase(itemName);
            if(info==null){
                info=new Info();
                info.setId((long)-1);
                info.setName(itemName);
                info.setSimpleInfo("-");
            }
            String replaceText=String.format(markdownTemplate,info.getId(),info.getName(), info.getSimpleInfo());
            content.replace(targetIdx[0],targetIdx[1],replaceText);
        }
        return content;
    }
    private StringBuilder makeMDofBold(StringBuilder content){
        String markdownTemplate="<span className='fw-bold'>%s</span>";
        Matcher matcher= Pattern.compile("\\*\\*(.+)\\*\\*").matcher(content);
        while (matcher.find()) {
            int[] targetIdx= {matcher.start(),matcher.end()};
            String innerText = matcher.group(1);
            String replaceText = String.format(markdownTemplate, innerText);
            content.replace(targetIdx[0],targetIdx[1],replaceText);
        }
        return content;
    }
    private StringBuilder makeMDofIndent(StringBuilder content) {
        Matcher matcher = Pattern.compile("^(\\s+)", Pattern.MULTILINE).matcher(content);
        while (matcher.find()) {
            String replaceText = "";
            int[] targetIdx = {matcher.start(1), matcher.end(1)};
            int spaces = matcher.group(1).length();
            replaceText += "&emsp;".repeat(spaces / 3);
            spaces %= 3;
            replaceText += "&ensp;".repeat(spaces / 2);
            spaces %= 2;
            replaceText += "&nbsp;".repeat(spaces);
            content.replace(targetIdx[0], targetIdx[1], replaceText);
        }
        return content;
    }
}
