package commontrade.communitytrade.controller;

import commontrade.commonyh.pojo.dto.*;
import commontrade.commonyh.pojo.entity.*;
import commontrade.commonyh.pojo.result.Result;
import commontrade.communitytrade.service.CommunityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import commontrade.commonyh.pojo.entity.*;

import javax.xml.stream.events.Comment;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Autowired
    private CommunityService communityService;


    @PostMapping("/selectPost")
    public  Result SelectPost(){
        return Result.success(communityService.getPostList());
    }

    @PostMapping("selectPostById")
    public  Result SelectPostById(@RequestBody Page page){
        return Result.success(communityService.selectPostById(page.getPage()));
    }
    // ====================== 1. 发布帖子 ======================
    @PostMapping("/insertPost")
    public Result insertPost(@RequestBody Post post) {
        boolean success = communityService.addPost(post);
        return Result.success(success);
    }

    // ====================== 2. 获取评论 ======================
    @PostMapping("/getComment")
    public Result getComment(@RequestBody CommentDTO dto) {
        Object data = switch (dto.getType()) {
            case "good" -> communityService.getGoodCommentList(dto.getId());
            case "user" -> communityService.getUserCommentList(dto.getId());
            case "post" -> communityService.getPostCommentList(dto.getId());
            default -> null;
        };
        return Result.success(data);
    }

    @PostMapping("/insertComment")
    public Result insertComment(@RequestBody CommentDTO commentdto) {
        System.out.println(commentdto);
        Object data = switch ( commentdto.getType()) {
            case "good" -> {
                CommentGood commentGood = new CommentGood();
                commentGood.setContent(commentdto.getContent());
                commentGood.setUserId(commentdto.getUserId());
                commentGood.setUserName(commentdto.getUserName());
                commentGood.setGoodId(commentdto.getId());
                commentGood.setGoodName(commentdto.getGoodName());
                commentGood.setAttitude(commentdto.getAttitude());
                yield communityService.insertCommentGood(commentGood);
            }
            case "user" -> {
                CommentUser commentUser = new CommentUser();
                commentUser.setContent(commentdto.getContent());
                commentUser.setUserId(commentdto.getUserId());
                commentUser.setUserName(commentdto.getUserName());
                commentUser.setToUserId(commentdto.getToUserId());
                commentUser.setToUserName(commentdto.getToUserName());
                commentUser.setAttitude(commentdto.getAttitude());
                yield communityService.insertCommentUser(commentUser);
            }
            case "post" -> {
                System.out.println(commentdto);
                CommentPost commentPost = new CommentPost();
                commentPost.setContent(commentdto.getContent());
                commentPost.setUserId(commentdto.getUserId());
                commentPost.setUserName(commentdto.getUserName());
                commentPost.setPostId(commentdto.getPostId());
                commentPost.setIndex(1);
                yield communityService.insertCommentPost(commentPost);
            }
            default -> null;
        };

        return Result.success();
    }
    // ====================== 3. 删除评论 ======================
    @PostMapping("/deleteComment")
    public Result deleteComment(@RequestBody CommentDTO dto) {
        String table = switch (dto.getType()) {
            case "good" -> "comment_good";
            case "user" -> "comment_user";
            case "post" -> "comment_post";
            default -> throw new RuntimeException("类型错误");
        };
        boolean success = communityService.deleteComment(table, dto.getId(), 1);
        return Result.success(success);
    }

    // ====================== 4. 举报 ======================
    @PostMapping("/report")
    public Result report(@RequestBody ReportDTO dto) {
        String table = switch (dto.getType()) {
            case "user" -> "user";
            case "post" -> "post";
            case "good" -> "comment_good";
            case "comment" -> "comment_post";
            default -> throw new RuntimeException("类型错误");
        };
        boolean success = communityService.reportComment(table, dto.getId());
        return Result.success(success);
    }

    @PostMapping("/insertCommentPost")
    public Result insertCommentPost(@RequestBody CommentPost commentPost) {
        boolean success = communityService.insertCommentPost(commentPost);
        return Result.success(success);
    }

    // ====================== 5. 点赞 ======================
    @PostMapping("/like")
    public Result like(@RequestBody LikeDTO dto) {
        Likes likes = new Likes();
        likes.setUserId(dto.getUserId());
        likes.setTargetId(dto.getTargetId());
        likes.setType(dto.getType());
        likes.setIp(dto.getIp());
        boolean msg = communityService.toggleLike(likes);
        return Result.success(msg);
    }

    // ====================== 6. 收藏 ======================
    @PostMapping("/favorite")
    public Result favorite(@RequestBody LikeDTO dto) {
        Favorite favorite = new Favorite();
        favorite.setUserId(dto.getUserId());
        favorite.setTargetId(dto.getTargetId());
        favorite.setType(dto.getType());
        favorite.setIp(dto.getIp());
        boolean msg = communityService.toggleFavorite(favorite);
        return Result.success(msg);
    }

}