package commontrade.communitytrade.service;

import commontrade.commonyh.pojo.dto.GoodDTO;
import commontrade.commonyh.pojo.dto.LoginDTO;
import commontrade.commonyh.pojo.entity.*;
import commontrade.commonyh.pojo.vo.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CommunityService {
    // ====================== 帖子 ======================
    List<Post> getPostList();
    boolean addPost(Post post);
    boolean deletePost(Integer id);
    boolean updatePost(Post post);

    // ====================== 评论 ======================
    List<CommentGood> getGoodCommentList(Integer id);
    List<CommentUser> getUserCommentList(Integer id);
    List<CommentPost> getPostCommentList(Integer id);

    public boolean insertCommentUser(CommentUser commentUser);
    public boolean insertCommentGood(CommentGood commentGood);


    Post selectPostById(Integer id);

    boolean deleteComment(String table, Integer id, Integer status);
    boolean reportComment(String table, Integer id);

    boolean insertCommentPost(CommentPost commentPost);

    // ====================== 点赞 / 收藏 ======================
    boolean toggleLike(Likes likes);
    boolean toggleFavorite(Favorite favorite);
}
