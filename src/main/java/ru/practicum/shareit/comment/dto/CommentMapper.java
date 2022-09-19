package ru.practicum.shareit.comment.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommentMapper {
    public static Comment toComment(CommentDtoIn commentDtoIn) {
        Comment comment = new Comment();
        comment.setText(commentDtoIn.getText());
        comment.setCreated(LocalDateTime.now());
        return comment;
    }

    public static List<CommentDtoOut> toCommentDtos(List<Comment> comments) {
        return comments.stream().map(CommentMapper::toCommentDtoOut).collect(Collectors.toList());
    }

    public static CommentDtoOut toCommentDtoOut(Comment comment) {
        var commentDtoOut = new CommentDtoOut();
        commentDtoOut.setId(comment.getId());
        commentDtoOut.setText(comment.getText());
        commentDtoOut.setItemName(comment.getItem().getName());
        commentDtoOut.setAuthorName(comment.getAuthor().getName());
        commentDtoOut.setCreated(comment.getCreated());
        return commentDtoOut;
    }
}
