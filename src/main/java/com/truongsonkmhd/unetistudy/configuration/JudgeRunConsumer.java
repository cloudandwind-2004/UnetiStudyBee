package com.truongsonkmhd.unetistudy.configuration;

import com.rabbitmq.client.Channel;
import com.truongsonkmhd.unetistudy.dto.coding_exercise_dto.JudgeRequestDTO;
import com.truongsonkmhd.unetistudy.dto.coding_exercise_dto.JudgeRunResponseDTO;
import com.truongsonkmhd.unetistudy.dto.judge_rabbit_mq.JudgeRunMessage;
import com.truongsonkmhd.unetistudy.service.JudgeService;
import com.truongsonkmhd.unetistudy.service.WebSocketNotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class JudgeRunConsumer {

    private final JudgeService judgeService;
    private final WebSocketNotificationService webSocketService;

    @RabbitListener(queues = JudgeRabbitConfig.QUEUE_RUN)
    public void consume(JudgeRunMessage payload, Message message, Channel channel) throws Exception {
        long tag = message.getMessageProperties().getDeliveryTag();
        log.info("Processing run job: runId={}, userId={}", payload.getRunId(), payload.getUserId());

        try {
            // 1) Thông báo trạng thái RUNNING
            webSocketService.notifyRunStatus(payload.getUserId(), payload.getRunId(), "RUNNING");

            // 2) Build request
            JudgeRequestDTO req = JudgeRequestDTO.builder()
                    .exerciseId(payload.getExerciseId())
                    .language(payload.getLanguage())
                    .sourceCode(payload.getCode())
                    .testCaseInput(payload.getTestCaseInput())
                    .testCaseId(payload.getTestCaseId())
                    .build();

            // 3) Thực thi
            Object result;
            if (payload.getTestCaseInput() != null || payload.getTestCaseId() != null) {
                result = judgeService.runSingleTestCase(req);
            } else {
                result = judgeService.runUserCode(req);
            }

            // 4) Gửi kết quả qua WebSocket
            webSocketService.notifyRunResult(payload.getUserId(), result);

            // 5) ACK
            channel.basicAck(tag, false);
            log.info("Run job completed: runId={}", payload.getRunId());

        } catch (Exception e) {
            log.error("Run job failed: runId={}", payload.getRunId(), e);
            
            // Gửi thông báo trạng thái lỗi
            webSocketService.notifyRunStatus(payload.getUserId(), payload.getRunId(), "ERROR");
            
            // QUAN TRỌNG: Gửi thêm một object kết quả giả với verdict là ERROR 
            // để Frontend nhận diện được và tắt spinner (vì Frontend lắng nghe runResult để tắt spinner)
            JudgeRunResponseDTO errorResult = JudgeRunResponseDTO.builder()
                    .verdict("RUNTIME_ERROR")
                    .status("ERROR")
                    .message("Lỗi hệ thống khi thực thi: " + e.getMessage())
                    .build();
            webSocketService.notifyRunResult(payload.getUserId(), errorResult);

            channel.basicAck(tag, false);
        }
    }
}
